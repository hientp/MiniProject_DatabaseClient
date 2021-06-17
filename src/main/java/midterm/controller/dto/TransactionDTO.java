package midterm.controller.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransactionDTO {

    @NotNull(message = "You must supply a amount!")
    private BigDecimal amount;

    @NotNull(message = "You must supply a senderAccountId!")
    private Integer senderAccountId;

    @NotNull(message = "You must supply a receiverAccountId!")
    private Integer receiverAccountId;

    public TransactionDTO() {
    }

    public TransactionDTO(BigDecimal amount, Integer senderAccountId, Integer receiverAccountId) {
        this.amount = amount;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Integer senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Integer getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Integer receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }
}
