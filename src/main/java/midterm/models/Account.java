package midterm.models;

import midterm.models.enums.Status;
import midterm.models.enums.Typ;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity(name="account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    Integer userID;

    @Enumerated(EnumType.STRING)
    private Status status= Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    private Typ typ;

    @NotNull
    private BigDecimal balance;

    public Account() {
    }

    public Account(Integer userID, Status status, Typ typ, BigDecimal balance) {
        this.userID = userID;
        this.status = status;
        this.typ = typ;
        this.balance = balance;
    }

    public Account(Integer id, Integer userID, Status status, Typ typ, BigDecimal balance) {
        this.id = id;
        this.userID = userID;
        this.status = status;
        this.typ = typ;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Typ getTyp() {
        return typ;
    }

    public void setTyp(Typ typ) {
        this.typ = typ;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if(balance.compareTo(new BigDecimal("0"))>=0) {
            this.balance = balance;
        } else {
            throw new RuntimeException("The balance of this account would drop below 0! Transaction denied.");
        }
    }

    public void changeBalance(BigDecimal valueToChange) throws RuntimeException{
        setBalance(getBalance().add(valueToChange));
    }
}
