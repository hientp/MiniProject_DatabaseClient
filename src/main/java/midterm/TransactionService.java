package midterm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionService {

    public static void main(String[] args) {
        SpringApplication.run(TransactionService.class, args);
    }
    //--spring.profiles.active=patrick


    //a) Generate TestData
    //http://localhost:8080/banking/testData/
    //b) Get Account Information for a user
    //All CheckingAccounts, SavingsAccounts, CreditCards + All accounts for a primaryuser
    //http://localhost:8080/banking/checking_accounts/ +  http://localhost:8080/banking/checking_accounts/?user=1
    //http://localhost:8080/banking/savings_accounts/ +  http://localhost:8080/banking/checking_accounts/?user=1
    //http://localhost:8080/banking/credit_cards/ +  http://localhost:8080/banking/checking_accounts/?user=1
    //Create new account
    //http://localhost:8080/banking/account/new_checking_account/
    //http://localhost:8080/banking/account/new_savings_account/
    //http://localhost:8080/banking/account/new_credit_card/



    //Example Checking_Account:



    //Check balance of all accounts
    //http://localhost:8080/banking/account_balance/{id}


}