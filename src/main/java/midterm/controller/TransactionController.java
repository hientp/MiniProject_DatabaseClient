package midterm.controller;

import midterm.controller.dto.*;
import midterm.models.Account;
import midterm.models.Transaction;
import midterm.models.enums.Status;
import midterm.repository.AccountRepository;
import midterm.repository.TransactionRepository;
import midterm.service.TransactionFunctionalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionFunctionalityService transactionFunctionalityService;


    //Übertragung von Geld
    @PostMapping("/banking/transferMoney/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Transaction> transferMoney(@RequestBody @Valid TransactionDTO transactionDTO) {
        return transactionFunctionalityService.transferMoney(transactionDTO);
    }

    //Erhalte AccountInformationen
    @GetMapping("/banking/transactions/getaccount/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Account> getTransactionAccount(@RequestParam Integer id) {
        return transactionFunctionalityService.findAccountById(id,accountRepository);
    }

    //Erhalte AccountInformationen
    @PostMapping("/banking/transactions/postaccount/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Account> postTransactionAccount(@RequestParam Integer id,@RequestBody AccountDTO accountDTO) {
        return transactionFunctionalityService.postTransactionAccount(accountDTO);
    }

    //Modify balance checking account
    @PatchMapping("/banking/transactions_account_balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account modifyAccountBalance(@PathVariable Integer id, @RequestBody @Valid BalanceDTO balanceDTO)  {
        return transactionFunctionalityService.modifyAccountBalance(id,balanceDTO);
    }

    //Modify status account
//    @PatchMapping("/banking/transactions_account_status/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Account modifyAccountStatus(@PathVariable Integer id, @RequestBody Status status)  {
//        return transactionFunctionalityService.modifyAccountStatus(id,status);
//    }




}
