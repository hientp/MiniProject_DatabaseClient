package midterm.repository;

import midterm.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    Account findAccountById(Integer id);

    Integer findUserIdById(Integer AccountId);
}
