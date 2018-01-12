package de.lalo.jpa.account.boundary;

import de.lalo.jpa.account.entity.Transaction;

/**
 * @author llorenzen
 * @since 30.12.17
 */
public class TransactionChangedEvent {

    private Transaction transaction;

    public TransactionChangedEvent(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
