package midterm.repository;

import midterm.models.*;


import midterm.models.enums.Alignment;
import midterm.models.enums.Status;
import midterm.models.enums.Typ;
import midterm.service.TransactionFunctionalityService;
import org.apache.tomcat.jni.Address;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = {"patrick","hien","stefan"})
class TransactionRepositoryTest {

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
        //Init addresses
//        CA_1= new Account(0,1, Status.ACTIVE, Typ.CHECKING_ACCOUNT,new BigDecimal("2000"));
//        SA_1= new Account(1,1, Status.ACTIVE, Typ.SAVINGS_ACCOUNT,new BigDecimal("10000"));
//        CC_1= new Account(2,1, Status.ACTIVE, Typ.CREDIT_CARD,new BigDecimal("3000"));
        CA_1= new Account(1, Status.ACTIVE, Typ.CHECKING_ACCOUNT,new BigDecimal("2000"));
        SA_1= new Account(1, Status.ACTIVE, Typ.SAVINGS_ACCOUNT,new BigDecimal("10000"));
        CC_1= new Account(1, Status.ACTIVE, Typ.CREDIT_CARD,new BigDecimal("3000"));

        accountRepository.save(CA_1);
        accountRepository.save(SA_1);
        accountRepository.save(CC_1);

        CA_2= new Account(2, Status.ACTIVE, Typ.CHECKING_ACCOUNT,new BigDecimal("1000"));
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
    }

    @Test
    public void testRepoTests() throws Exception {

        //Test normal functions
        Transaction transaction = new Transaction(java.time.LocalDateTime.now(),new BigDecimal("200"));
        transactionRepository.save(transaction);

        TransactionPartners transactionPartners1 = new TransactionPartners(CA_1,transaction, Alignment.SENDER);
        TransactionPartners transactionPartners2 = new TransactionPartners(CA_2,transaction,Alignment.RECEIVER);

        transactionPartnersRepository.save(transactionPartners1);
        transactionPartnersRepository.save(transactionPartners2);

        assertEquals(transactionRepository.findAll().size(),1);
        assertEquals(transactionPartnersRepository.findAll().size(),2);

        //Test utils functions
        //Default Balances CA1: 2000, CA2:1000 ; minimumBalance: 250
        transactionFunctionalityService.transactMoney(accountRepository,CA_1,CA_2,transactionRepository,transactionPartnersRepository,new BigDecimal("500"));
        assertEquals(CA_1.getBalance(),new BigDecimal("1500"));
        assertEquals(CA_2.getBalance(),new BigDecimal("1500"));
        assertEquals(transactionRepository.findAll().size(),2);
        //Current Balances CA1: 1500, CA2:1500 ; minimumBalance: 250
        transactionFunctionalityService.transactMoney(accountRepository,CA_1,CA_2,transactionRepository,transactionPartnersRepository,new BigDecimal("1250"));
        assertEquals(CA_1.getBalance(),new BigDecimal("250"));
        assertEquals(CA_2.getBalance(),new BigDecimal("2750"));
        assertEquals(transactionRepository.findAll().size(),3);
        //Current Balances CA1: 250, CA2:2750 ; minimumBalance: 250
        transactionFunctionalityService.transactMoney(accountRepository,CA_1,CA_2,transactionRepository,transactionPartnersRepository,new BigDecimal("50"));
        assertEquals(CA_1.getBalance(),new BigDecimal("200"));
        assertEquals(CA_2.getBalance(),new BigDecimal("2800"));
        assertEquals(transactionRepository.findAll().size(),4);
        //Current Balances CA1: 160, CA2:2800 ; minimumBalance: 250
        transactionFunctionalityService.transactMoney(accountRepository,CA_2,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("2000"));
        assertEquals(CA_1.getBalance(),new BigDecimal("2200"));
        assertEquals(CA_2.getBalance(),new BigDecimal("800"));
        assertEquals(transactionRepository.findAll().size(),5);
        //Current Balances CA1: 2160, CA2:800 ; minimumBalance: 250
        Exception exception = assertThrows(Exception.class, () -> {
            transactionFunctionalityService.transactMoney(accountRepository,CA_2,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("1000"));
        });

        assertTrue(exception.getMessage().contains("The balance of this account would drop below 0! Transaction denied."));
        assertEquals(transactionRepository.findAll().size(),5);


    }

    @Test
    public void testFraudDetectionSQL() throws Exception {
        //Get MaxTransactionAmount per Day
        LocalDateTime currentTime =LocalDateTime.now();
        //Get SumTransactionAmount For this day
        LocalDate today = LocalDateTime.now().toLocalDate();
        transactionFunctionalityService.transactMoney(accountRepository,CA_1,CA_2,transactionRepository,transactionPartnersRepository,new BigDecimal("500"));
        List<Object[]> objList = transactionRepository.getMaxTransactionAmountPerDay(1,today.plusDays(1));
        assertEquals(objList.get(0)[0],new BigDecimal("2500.00"));

        List<Object[]> objList1 = transactionRepository.getSumTransactionAmountForThisDay(1,today);
        assertEquals(objList1.get(0)[1],new BigDecimal("2500.00"));

        //Get last TimeStamp
        List<Object[]> objList2 = transactionRepository.getLastTimeStampOfTransactions(CA_1.getId());
        Object dateTimeObject = objList2.get(0)[0];
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                // date / time
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                // nanoseconds, with minimum 1 and maximum 9 digits and a decimal point
                .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                // create formatter
                .toFormatter();
        LocalDateTime dateTime= LocalDateTime.parse(dateTimeObject.toString(), formatter);
        LocalDateTime l1 = LocalDateTime.parse(dateTimeObject.toString(), formatter);
        assertEquals(l1.withNano(0),currentTime.withNano(0));

    }


}