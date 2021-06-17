package midterm.controller;

import midterm.controller.dto.*;
import midterm.models.Transaction;
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


    //Anlegen von neuem checkingAccount
    @PostMapping("/banking/transferMoney/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Transaction> transferMoney(@RequestBody @Valid TransactionDTO transactionDTO) {
        return transactionFunctionalityService.transferMoney(transactionDTO);
    }




}
