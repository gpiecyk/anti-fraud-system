package antifraud.transaction.data;

import java.io.Serializable;

public class TransactionData implements Serializable {

    private Long amount;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
