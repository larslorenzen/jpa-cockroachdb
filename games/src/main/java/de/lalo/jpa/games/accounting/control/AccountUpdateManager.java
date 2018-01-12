package de.lalo.jpa.games.accounting.control;

import de.lalo.jpa.games.accounting.boundary.AccountStatusClient;
import de.lalo.jpa.games.accounting.boundary.TransactionChangeRecord;
import de.lalo.jpa.games.accounting.entity.Account;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author llorenzen
 * @since 12.01.18
 */
@Stateless
public class AccountUpdateManager {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private AccountStatusClient accountStatusClient;

    @Inject
    private Event<TransactionEvent> transactionEvent;

    public BookingResult performBookingOf(String playerId, long stake) {
        Account account = null;
        try {
            account = loadAccount(playerId);
        } catch (Exception e) {
            return BookingResult.failed(0);
        }
        return performBooking(account, stake);
    }

    private Account loadAccount(String playerId) {
        Account account = null;
        try {
            account = Account.findByPlayerId(entityManager, playerId);
        } catch (NoResultException e) {
            Long accountStatus = accountStatusClient.getCurrentBalance(playerId);
            if (accountStatus == null) {
                throw new IllegalStateException("Account not created yet");
            }
            account = new Account(playerId, accountStatus);
            entityManager.persist(account);
        }
        return account;
    }

    private BookingResult performBooking(Account account, long stake) {
        if (account.getBalance() >= stake) {
            transactionEvent.fire(new TransactionEvent(account.getPlayerId(), stake));
            account.reduceBalance(stake);
            return BookingResult.sucess(account.getBalance());
        }
        return BookingResult.failed(account.getBalance());
    }

    public void update(TransactionChangeRecord changeRecord) {
        Account account = loadAccount(changeRecord.getPlayerId());
        account.updateBalance(changeRecord.getPostBalance());
    }
}
