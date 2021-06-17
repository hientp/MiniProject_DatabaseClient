package midterm.repository;

import midterm.models.Transaction;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {


    @Query(value = "Select max(amount_per_day)\n" +
            "FROM\n" +
            "(Select day1, sum(amount) as amount_per_day\n" +
            "FROM\n" +
            "(Select DATE(date_time_of_transaction) as day1, account_id, amount\n" +
            "FROM\n" +
            "((Select account_id, transaction_id\n" +
            "FROM\n" +
            "(Select id\n" +
            "FROM account\n" +
            "where user_id=:user_id) acc\n" +
            "LEFT JOIN (Select * FROM transaction_partners where alignment='SENDER') tp\n" +
            "ON acc.id=tp.account_id)) t1 \n" +
            "LEFT JOIN transaction t2\n" +
            "ON t1.transaction_id=t2.id) all_transactions\n" +
            "where day1!=:thisDay\n" +
            "Group By day1) amount_per_day", nativeQuery = true)
    List<Object[]> getMaxTransactionAmountPerDay(@Param("user_id") Integer user_id, @Param("thisDay") LocalDate thisDay);

    @Query(value = "Select day1, sum(amount) as amount_per_day\n" +
            "FROM\n" +
            "(Select DATE(date_time_of_transaction) as day1, account_id, amount\n" +
            "FROM\n" +
            "((Select account_id, transaction_id\n" +
            "FROM\n" +
            "(Select id\n" +
            "FROM\n" +
            "(Select id\n" +
            "FROM account\n" +
            "where user_id=:user_id) acc\n" +
            "LEFT JOIN (Select * FROM transaction_partners where alignment='SENDER') tp\n" +
            "ON acc.id=tp.account_id)) t1 \n" +
            "LEFT JOIN transaction t2\n" +
            "ON t1.transaction_id=t2.id) all_transactions\n" +
            "WHERE all_transactions.day1=:thisDay\n" +
            "Group By day1", nativeQuery = true)
    List<Object[]> getSumTransactionAmountForThisDay(@Param("user_id") Integer user_id, @Param("thisDay") LocalDate thisDay);

    @Query(value = "Select max(date_time_of_transaction)\n" +
            "FROM\n" +
            "(Select account_id, date_time_of_transaction,amount\n" +
            "FROM\n" +
            "(Select * FROM transaction_partners where alignment='SENDER' and account_id=:account_id) tp\n" +
            "LEFT JOIN transaction t2\n" +
            "ON tp.transaction_id=t2.id) all_transactions\n" , nativeQuery = true)
    List<Object[]> getLastTimeStampOfTransactions(@Param("account_id") Integer account_id);





}
