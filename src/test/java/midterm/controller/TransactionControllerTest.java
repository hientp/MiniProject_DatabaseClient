package midterm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import midterm.TransactionService;
import midterm.controller.dto.TransactionDTO;

import midterm.models.Account;
import midterm.models.enums.Status;

import midterm.models.enums.Typ;
import midterm.repository.*;
import midterm.service.TransactionFunctionalityService;
import org.apache.tomcat.jni.Address;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = {"patrick","hien","stefan"})
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionFunctionalityService transactionFunctionalityService;
    @Autowired
    TransactionPartnersRepository transactionPartnersRepository;

    Address address1,address2;
    Account CA_1,CA_2,CA_3;
    Account SA_1, SA_2, SA_3;
    Account CC_1, CC_2, CC_3;

    @BeforeAll
    public void setUp() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //Init addresses
        CA_1= new Account(1, Status.ACTIVE, Typ.CHECKING_ACCOUNT,new BigDecimal("2000000"));
        SA_1= new Account(1, Status.ACTIVE, Typ.SAVINGS_ACCOUNT,new BigDecimal("1000000"));
        CC_1= new Account(1, Status.ACTIVE, Typ.CREDIT_CARD,new BigDecimal("3000000"));

        accountRepository.save(CA_1);
        accountRepository.save(SA_1);
        accountRepository.save(CC_1);


        CA_2= new Account(2, Status.ACTIVE, Typ.CHECKING_ACCOUNT,new BigDecimal("3000"));
        SA_2= new Account(2, Status.ACTIVE, Typ.SAVINGS_ACCOUNT,new BigDecimal("20000"));
        CC_2= new Account(2, Status.ACTIVE, Typ.CREDIT_CARD,new BigDecimal("1000"));

        accountRepository.save(CA_2);
        accountRepository.save(SA_2);
        accountRepository.save(CC_2);

        CA_3= new Account(3, Status.ACTIVE, Typ.CHECKING_ACCOUNT,new BigDecimal("20000"));
        SA_3= new Account(3, Status.ACTIVE, Typ.SAVINGS_ACCOUNT,new BigDecimal("100000"));
        CC_3= new Account(3, Status.ACTIVE, Typ.CREDIT_CARD,new BigDecimal("6000"));

        accountRepository.save(CA_3);
        accountRepository.save(SA_3);
        accountRepository.save(CC_3);

        //Provide Transaction History for account CA_2
        //Start of transaction history
        LocalDateTime historyStart = LocalDateTime.now().minusYears(1);
        //CA_1 3000
        //Send 20€ each hour and receive each 100th hour 3000€
        for (int i=0; i<100; i++) {
            transactionFunctionalityService.transactMoney(historyStart.plusHours(i), accountRepository, CA_2, CA_1, transactionRepository, transactionPartnersRepository, new BigDecimal("20"));
            if(i % 100 == 0){
                transactionFunctionalityService.transactMoney(accountRepository, CA_1, CA_2, transactionRepository, transactionPartnersRepository, new BigDecimal("3000"));
            }
            System.out.println(i+": "+CA_2.getBalance());
        }

    }


    @Test
    void transferMoney() throws Exception {
        //Max amount 480=20*24 --> 1.5 = 480+240=720
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(new BigDecimal("720"));
        transactionDTO.setSenderAccountId(4);
        transactionDTO.setReceiverAccountId(2);

        String jsonString = objectMapper.writeValueAsString(transactionDTO);
        ResultActions checkMock =mockMvc.perform(post("/banking/transferMoney/")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(new BigDecimal("1"));
        transactionDTO.setSenderAccountId(4);
        transactionDTO.setReceiverAccountId(2);

        jsonString = objectMapper.writeValueAsString(transactionDTO);

        mockMvc.perform(post("/banking/transferMoney/")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Suspicious behaviour was detected. Account was frozen. Please contact your banking advisor.")));

        mockMvc.perform(post("/banking/transferMoney/")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Account is frozen. Please contact your banking advisor.")));

    }
}