package de.lalo.jpa.account.boundary;

/**
 * @author llorenzen
 * @since 30.12.17
 */
public class TransactionChangedMessage {

    private String id;

    private String reference;

    private long preBalance;

    private long postBalance;

    private long amount;

    private String state;

    public TransactionChangedMessage() {
        // for jaxb or so
    }

    public TransactionChangedMessage(String id, String reference, long preBalance, long postBalance, long amount, String state) {
        this.id = id;
        this.reference = reference;
        this.preBalance = preBalance;
        this.postBalance = postBalance;
        this.amount = amount;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public long getPreBalance() {
        return preBalance;
    }

    public long getPostBalance() {
        return postBalance;
    }

    public long getAmount() {
        return amount;
    }

    public String getState() {
        return state;
    }
}
