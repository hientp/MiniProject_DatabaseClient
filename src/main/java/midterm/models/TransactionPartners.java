package midterm.models;

import midterm.models.enums.Alignment;

import javax.persistence.*;

@Entity(name="TransactionPartners")
@Table(name = "transaction_partners")
public class TransactionPartners {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name="transaction_id")
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    private Alignment alignment;

    public TransactionPartners() {
    }

    public TransactionPartners(Account account, Transaction transaction, Alignment alignment) {
        this.account = account;
        this.transaction = transaction;
        this.alignment = alignment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
