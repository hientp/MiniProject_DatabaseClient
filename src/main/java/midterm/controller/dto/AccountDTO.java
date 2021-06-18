package midterm.controller.dto;

import midterm.models.enums.Status;
import midterm.models.enums.Typ;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AccountDTO {

    @NotNull(message = "You must supply an in!")
    Integer id;

    @NotNull(message = "You must supply an userId!")
    Integer userId;

    @NotNull(message = "You must supply a status!")
    private Status status;

    @NotNull(message = "You must supply a type!")
    private Typ typ;

    @NotNull(message = "You must supply an balance!")
    private BigDecimal balance;



    public AccountDTO() {
    }

    public AccountDTO(Integer id, Integer userId, Status status, Typ typ, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        this.balance = balance;
    }
}
