package midterm;

import midterm.models.*;
import midterm.models.enums.Status;
import midterm.models.enums.Typ;
import midterm.repository.*;
import midterm.service.TransactionFunctionalityService;
import org.apache.tomcat.jni.Address;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = {"patrick","hien","stefan"})
class FraudDetectionTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionPartnersRepository transactionPartnersRepository;
    @Autowired
    TransactionFunctionalityService transactionFunctionalityService;

    Address address1,address2;
    Account CA_1,CA_2,CA_3;
    Account SA_1, SA_2, SA_3;
    Account CC_1, CC_2, CC_3;

    @BeforeAll
    public void setUp() throws Exception{
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
        for (int i=0; i<500; i++) {
            transactionFunctionalityService.transactMoney(historyStart.plusHours(i), accountRepository, CA_2, CA_1, transactionRepository, transactionPartnersRepository, new BigDecimal("20"));
            if(i % 100 == 0){
                transactionFunctionalityService.transactMoney(accountRepository, CA_1, CA_2, transactionRepository, transactionPartnersRepository, new BigDecimal("3000"));
            }
            System.out.println(i+": "+CA_2.getBalance());
        }

    }

    @Test
    public void testAmountFraud() throws Exception {
        //Max amount 480=20*24 --> 1.5 = 480+240=720
        transactionFunctionalityService.transactMoneySecured(LocalDateTime.now(), accountRepository, CA_2, CA_1, transactionRepository, transactionPartnersRepository, new BigDecimal("720"));
        List<Transaction> transactionList = transactionRepository.findAll();
        assertEquals(transactionList.get(transactionList.size()-1).getAmount(),new BigDecimal("720.00"));
        assertEquals(CA_2.getStatus(),Status.ACTIVE);

        Exception exception = assertThrows(Exception.class, () -> {
            transactionFunctionalityService.transactMoneySecured(LocalDateTime.now(), accountRepository,CA_2,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("1"));
        });

        assertTrue(exception.getMessage().contains("Suspicious behaviour was detected. Account was frozen. Please contact your banking advisor."));
        assertEquals(CA_2.getStatus(),Status.FROZEN);

        exception = assertThrows(Exception.class, () -> {
            transactionFunctionalityService.transactMoneySecured(LocalDateTime.now(), accountRepository,CA_2,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("1"));
        });
        assertTrue(exception.getMessage().contains("Account is frozen. Please contact your banking advisor."));

    }

    @Test
    public void testSameSecond() throws Exception {
        LocalDateTime testTime= LocalDateTime.now();
        transactionFunctionalityService.transactMoneySecured(testTime, accountRepository, CA_3, CA_1, transactionRepository, transactionPartnersRepository, new BigDecimal("2"));

        Exception exception = assertThrows(Exception.class, () -> {
            transactionFunctionalityService.transactMoneySecured(testTime, accountRepository,CA_3,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("2"));
        });

        assertTrue(exception.getMessage().contains("Suspicious behaviour was detected. Account was frozen. Please contact your banking advisor."));
        assertEquals(CA_3.getStatus(),Status.FROZEN);
    }



}