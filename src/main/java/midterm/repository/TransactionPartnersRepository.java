package midterm.repository;

import midterm.models.Transaction;
import midterm.models.TransactionPartners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionPartnersRepository extends JpaRepository<TransactionPartners,Integer> {
}
