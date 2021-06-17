package midterm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Transaction")
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime dateTimeOfTransaction;

    @OneToMany(mappedBy="transaction")
    @JsonIgnore
    private List<TransactionPartners> transactionPartners;

    private BigDecimal amount;

    public Transaction() {
    }

    public Transaction(LocalDateTime dateTimeOfTransaction, BigDecimal amount) {
        this.dateTimeOfTransaction = dateTimeOfTransaction;
          this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeOfTransaction() {
        return dateTimeOfTransaction;
    }

    public void setDateTimeOfTransaction(LocalDateTime dateTimeOfTransaction) {
        this.dateTimeOfTransaction = dateTimeOfTransaction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
