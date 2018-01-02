package de.lalo.jpa.account.control;

import de.lalo.jpa.account.boundary.TransactionChangedEvent;
import de.lalo.jpa.account.boundary.TransactionFailure;
import de.lalo.jpa.account.boundary.TransactionMessage;
import de.lalo.jpa.account.entity.Account;
import de.lalo.jpa.account.entity.Transaction;
import de.lalo.jpa.player.entity.Player;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author llorenzen
 * @since 30.12.17
 */
@Stateless
public class AccountingService {

    @Inject
    private Event<TransactionChangedEvent> transactionChangedEvent;

    @Inject
    private Event<TransactionFailure> transactionFailedEvent;

    @PersistenceContext
    private EntityManager entityManager;

    public void performTransaction(TransactionMessage transactionMessage) {
        Account account = null;
        try {
            account = Account.findByPlayerId(entityManager, transactionMessage.getPlayerId());
        } catch (NoResultException e) {
            transactionFailedEvent.fire(new TransactionFailure(transactionMessage, e.getMessage()));
            return;
        }
        Transaction transaction = new Transaction(account, transactionMessage.getId(), transactionMessage.getPayable(), transactionMessage.getNonPayable(), transactionMessage.getDescription());
        transaction.perform();
        entityManager.persist(transaction);
        entityManager.persist(account);

        transactionChangedEvent.fire(new TransactionChangedEvent(transaction));
    }

    public void createAccount(Player player, String currency) {
        Account account = new Account(player, currency);
        entityManager.persist(account);
    }
}
