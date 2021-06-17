//package midterm.service;
//
//import midterm.models.Transaction;
//import midterm.models.TransactionPartners;
//import midterm.models.enums.Status;
//import midterm.repository.*;
//import org.apache.tomcat.jni.Address;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class TestData {
//    @Autowired
//    TransactionRepository transactionRepository;
//    @Autowired
//    TransactionPartnersRepository transactionPartnersRepository;
//    @Autowired
//    TransactionService transactionService;
//    @Autowired
//    AccountRepository accountRepository;
//
//    //Init class instances
//    List<Address> addressList = new ArrayList<Address>();
//    Transaction transaction;
//    TransactionPartners transactionPartner1, transactionPartner2;
//
//
//    public void generateData() {
//        try {
//            //Accounts
//            for(int i=0;i<50;i++) {
//                if(i<30) {
//                    checkingAccountList.add(new CheckingAccount(balance0, "secretKey_" + i, accountHolderList.get(i), null, Status.ACTIVE));
//                    creditCardList.add(new CreditCard(balance1,"secretKeyY_"+i,accountHolderList.get(i),null,interestRate1,creditLimit1));
//                    accountRepository.save(creditCardList.get(i));
//                } else {
//                    checkingAccountList.add(new CheckingAccount(balance0, "secretKey_" + i, accountHolderList.get(i), accountHolderList.get(i-20), Status.ACTIVE));
//                    savingsAccountList.add(new SavingsAccount(balance0,"secretKeyY_"+i,accountHolderList.get(i),accountHolderList.get(i-20),interestRate2,minimumBalance1,Status.ACTIVE));
//                    accountRepository.save(savingsAccountList.get(i-30));
//                }
//                accountRepository.save(checkingAccountList.get(i));
//
//
//            }
//            //Transactions
//            BigDecimal amount;
//            for (int i = 0; i < 25; i++) {
//                amount = new BigDecimal("10").multiply(BigDecimal.valueOf(i));
//                transactionService.transactMoney(accountRepository,checkingAccountList.get(i),checkingAccountList.get(i+25), transactionRepository, transactionPartnersRepository, amount);
//            }
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data already exists.");
//        }
//    }
//
//
//}
