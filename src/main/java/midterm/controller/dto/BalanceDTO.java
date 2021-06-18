package midterm.controller.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BalanceDTO {

    @NotNull(message = "You must supply a balance!")
    private BigDecimal balance;

    public BalanceDTO() {
    }

    public BalanceDTO(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
