//package midterm;
//
//import midterm.models.*;
//import midterm.models.accounts.CheckingAccount;
//import midterm.models.accounts.CreditCard;
//import midterm.models.accounts.SavingsAccount;
//import midterm.models.enums.Status;
//import midterm.models.users.AccountHolder;
//import midterm.repository.*;
//import org.apache.tomcat.jni.Address;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ActiveProfiles(profiles = {"patrick","hien","stefan"})
//class FraudDetectionTest {
//
//    @Autowired
//    AccountRepository accountRepository;
//    @Autowired
//    TransactionRepository transactionRepository;
//    @Autowired
//    TransactionPartnersRepository transactionPartnersRepository;
//
//    Address address1,address2;
//    CheckingAccount CA_1,CA_2,CA_3;
//    SavingsAccount SA_1, SA_2, SA_3;
//    CreditCard CC_1, CC_2, CC_3;
//
//    @BeforeAll
//    public void setUp() throws Exception{
//        //Init addresses
//        address1= new Address("Default Str.1","Berlin","Germany","15xxx");
//        address2= new Address("Default Str.2","Berlin","Germany","15xxx");
//        a1 = new AccountHolder("Anton Alligator",new Date(100,5,10),address1);
//        CA_1 = new CheckingAccount(new BigDecimal("2000000"),"secretKey123",a1,null, Status.ACTIVE);
//        SA_1 = new SavingsAccount( new BigDecimal("1000000"),"banane123",a1,null,new BigDecimal("0.003"),new BigDecimal("1000"),Status.ACTIVE);
//        CC_1= new CreditCard( new BigDecimal("3000000"),"obst321",a1,null,new BigDecimal("0.2"),new BigDecimal("200"));
//
//        addressRepository.save(address1);
//        userRepository.save(a1);
//        accountRepository.save(CA_1);
//        accountRepository.save(SA_1);
//        accountRepository.save(CC_1);
//
//        a2 = new AccountHolder("Bernd Babyelefant",new Date(100,5,10),address2);
//        CA_2 = new CheckingAccount(new BigDecimal("3000"),"secretKey456",a2,null, Status.ACTIVE);
//        SA_2 = new SavingsAccount( new BigDecimal("20000"),"banane456",a2,null,new BigDecimal("0.001"),new BigDecimal("1000"),Status.ACTIVE);
//        CC_2= new CreditCard( new BigDecimal("1000"),"obst654",a2,null,new BigDecimal("0.1"),new BigDecimal("300"));
//
//        addressRepository.save(address2);
//        userRepository.save(a2);
//        accountRepository.save(CA_2);
//        accountRepository.save(SA_2);
//        accountRepository.save(CC_2);
//
//        a3 = new AccountHolder("Chrissy Chamäleon",new Date(900,2,10),address1);
//        CA_3 = new CheckingAccount(new BigDecimal("20000"),"secretKey789",a3,null, Status.ACTIVE);
//        SA_3 = new SavingsAccount( new BigDecimal("100000"),"banane789",a3,null,new BigDecimal("0.002"),new BigDecimal("500"),Status.ACTIVE);
//        CC_3= new CreditCard( new BigDecimal("6000"),"obst987",a3,null,new BigDecimal("0.2"),new BigDecimal("400"));
//
//        userRepository.save(a3);
//        accountRepository.save(CA_3);
//        accountRepository.save(SA_3);
//        accountRepository.save(CC_3);
//
//        //Provide Transaction History for account CA_2
//        //Start of transaction history
//        LocalDateTime historyStart = LocalDateTime.now().minusYears(1);
//        //CA_1 3000
//        //Send 20€ each hour and receive each 100th hour 3000€
//        for (int i=0; i<500; i++) {
//            Utils.transactMoney(historyStart.plusHours(i), accountRepository, CA_2, CA_1, transactionRepository, transactionPartnersRepository, new BigDecimal("20"));
//            if(i % 100 == 0){
//                Utils.transactMoney(accountRepository, CA_1, CA_2, transactionRepository, transactionPartnersRepository, new BigDecimal("3000"));
//            }
//            System.out.println(i+": "+CA_2.getBalance());
//        }
//
//    }
//
//    @Test
//    public void testAmountFraud() throws Exception {
//        //Max amount 480=20*24 --> 1.5 = 480+240=720
//        Utils.transactMoneySecured(LocalDateTime.now(), accountRepository, CA_2, CA_1, transactionRepository, transactionPartnersRepository, new BigDecimal("720"));
//        List<Transaction> transactionList = transactionRepository.findAll();
//        assertEquals(transactionList.get(transactionList.size()-1).getAmount(),new BigDecimal("720.00"));
//        assertEquals(CA_2.getStatus(),Status.ACTIVE);
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            Utils.transactMoneySecured(LocalDateTime.now(), accountRepository,CA_2,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("1"));
//        });
//
//        assertTrue(exception.getMessage().contains("Suspicious behaviour was detected. Account was frozen. Please contact your banking advisor."));
//        assertEquals(CA_2.getStatus(),Status.FROZEN);
//
//        exception = assertThrows(Exception.class, () -> {
//            Utils.transactMoneySecured(LocalDateTime.now(), accountRepository,CA_2,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("1"));
//        });
//        assertTrue(exception.getMessage().contains("Account is frozen. Please contact your banking advisor."));
//
//    }
//
//    @Test
//    public void testSameSecond() throws Exception {
//        LocalDateTime testTime= LocalDateTime.now();
//        Utils.transactMoneySecured(testTime, accountRepository, CA_3, CA_1, transactionRepository, transactionPartnersRepository, new BigDecimal("2"));
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            Utils.transactMoneySecured(testTime, accountRepository,CA_3,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("2"));
//        });
//
//        assertTrue(exception.getMessage().contains("Suspicious behaviour was detected. Account was frozen. Please contact your banking advisor."));
//        assertEquals(CA_2.getStatus(),Status.FROZEN);
//    }
//
//
//
//}