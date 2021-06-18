package midterm;

import midterm.models.Account;
import midterm.models.Transaction;
import midterm.models.TransactionPartners;
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
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = {"patrick","hien","stefan"})
public class GenerateTestData {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionPartnersRepository transactionPartnersRepository;
    @Autowired
    TransactionFunctionalityService transactionFunctionalityService;

    //Init class instances
    List<Address> addressList = new ArrayList<Address>();
    Transaction transaction;
    TransactionPartners transactionPartner1, transactionPartner2;
    List<Account> checkingAccountList = new ArrayList<Account>();
    List<Account> savingsAccountList = new ArrayList<Account>();
    List<Account> creditCardList = new ArrayList<Account>();

    @BeforeAll
    public void setUp() throws Exception{
        //set up all accounts
        BigDecimal balance0, balance1, interestRate1,interestRate2,creditLimit1,minimumBalance1;
        int j=67;
        for(int i=0;i<50;i++){
            balance0=new BigDecimal("500").multiply(BigDecimal.valueOf(i+1));
            balance1=new BigDecimal("200").multiply(BigDecimal.valueOf(i+1));
            interestRate1= new BigDecimal("0.1").add(new BigDecimal("0.0003").multiply(BigDecimal.valueOf(i)));
            interestRate2= new BigDecimal("0.0025").add(new BigDecimal("0.001").multiply(BigDecimal.valueOf(i)));
            minimumBalance1= new BigDecimal("100").add(new BigDecimal("10").multiply(BigDecimal.valueOf(i)));
            creditLimit1= new BigDecimal("100").add(new BigDecimal("1000").multiply(BigDecimal.valueOf(i)));
            if(i<30) {
                creditCardList.add(new Account(j,i+1, Status.ACTIVE,Typ.CREDIT_CARD,balance1));
                j++;
                checkingAccountList.add(new Account(j,i+1, Status.ACTIVE,Typ.CHECKING_ACCOUNT,balance0));
                j++;
                accountRepository.save(creditCardList.get(i));
            } else {
                savingsAccountList.add(new Account(j,i+1, Status.ACTIVE,Typ.SAVINGS_ACCOUNT,balance1));
                j++;
                checkingAccountList.add(new Account(j,i+1, Status.ACTIVE,Typ.CHECKING_ACCOUNT,balance0));
                j++;
                accountRepository.save(savingsAccountList.get(i-30));
            }
            accountRepository.save(checkingAccountList.get(i));
        }
        //Transactions
        BigDecimal amount;
        for(int i=0;i<25;i++){
            amount=new BigDecimal("10").multiply(BigDecimal.valueOf(i));
            transactionFunctionalityService.transactMoney(accountRepository,checkingAccountList.get(i),checkingAccountList.get(i+25),transactionRepository,transactionPartnersRepository,amount);
        }

    }

    @Test
    public void checkEntries(){

    }



}
