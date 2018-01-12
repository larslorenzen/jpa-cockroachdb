package de.lalo.jpa.account.boundary;

/**
 * @author llorenzen
 * @since 02.01.18
 */
public class TransactionFailure {

    private TransactionMessage transactionMessage;

    private String failure;

    public TransactionFailure(TransactionMessage transactionMessage, String failure) {
        this.transactionMessage = transactionMessage;
        this.failure = failure;
    }

    public TransactionMessage getTransactionMessage() {
        return transactionMessage;
    }

    public String getFailure() {
        return failure;
    }
}
