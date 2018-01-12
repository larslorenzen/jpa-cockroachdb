package de.lalo.jpa.account.entity;

import de.lalo.jpa.player.entity.Player;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.*;

/**
 * @author llorenzen
 * @since 23.10.17
 */
@Entity
@Table(name = "accounts", indexes = {
        @Index(columnList = "playerReference"),
        @Index(columnList = "player_id")
})
@NamedQueries(@NamedQuery(name = "findByPlayerId", query = "SELECT a FROM Account AS a WHERE a.playerReference = :playerId"))
public class Account implements Serializable {

    private static final Logger logger = Logger.getLogger(Account.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    private String playerReference;

    private long payable;

    private long nonPayable;

    private String currency;

    @Version
    private long version;

    public Account() {
        // for JPA
    }

    public Account(Player player, String currency) {
        this.player = player;
        this.playerReference = player.getPlayerReference();
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public void perform(Transaction transaction) {
        changeBalance(transaction);
    }

    private void changeBalance(Transaction transaction) {
        logger.entering(Account.class.getName(), "changeBalance");
        logger.log(Level.FINEST, "changing balance of account [Id: {0}" + "] with " + "amount: " + "{1}", new Object[]{getId(), transaction.getAmount()});

        transaction.setPreBalance(getBalance());

        long txAmount = transaction.getAmount();

        // PayInTransaction -> +nonpayable
        // StakeTransaction -> -paypable
        // WinningsTransaction -> +payable
        // PayoutTransaction -> -payable

        long txNonPayable = transaction.getNonPayable();
        long txPayable = transaction.getPayable(); // +5

        long newPayable = payable + txPayable;
        long newNonPayable = nonPayable + txNonPayable;

        // assertions:
        // - only nonPayable can be negative.
        // - payable is always 0 if nonPayable is negative
        // - credit entries will always fill up nonPayable to 0 before the remaining amount is transferred on the


        // full adjustment only here, so only cancel transaction may set to different values
        if (newPayable < 0 || newNonPayable < 0) {
            if (newPayable < 0 && newNonPayable > 0) {
                if (txPayable < 0) {
                    // try to take from other account
                    long calcNewNonPayable = newNonPayable + txPayable;
                    if (calcNewNonPayable >= 0) {
                        transaction.setPayable(0);
                        transaction.setNonPayable(txNonPayable + txPayable);
                    } else {
                        // try to set payable to 0 and nonPayable to negative value
                        transaction.setPayable(txPayable - newPayable);
                        transaction.setNonPayable(txNonPayable + newPayable);
                    }
                }
            } else if (newNonPayable < 0 && newPayable > 0) {
                if (txNonPayable < 0) {
                    long calcPayable = newPayable + txNonPayable;
                    if (calcPayable >= 0) {
                        transaction.setNonPayable(0);
                        transaction.setPayable(txNonPayable + txPayable);
                    }
                    // if we come here: non payable account will be negative, but we also have money on the payable account
                    // -> so
                    // take always (everything we have) from non-payable -> then if it's not enough take more from the payable
                    // if this is also not enough take all payable and non-payable goes negative
                    long amount = txPayable + txNonPayable;
                    long calcNewNonPayable = nonPayable + amount;
                    if (calcNewNonPayable < 0) {
                        // non payable is not enough
                        long restForPayable = amount + nonPayable; // take all from non payable
                        long calcNewPayable = payable + restForPayable; // and try get much as possible from payable
                        if (calcNewPayable < 0) {
                            restForPayable = -payable;
                        }
                        transaction.setPayable(restForPayable); // and try get much as possible from payable
                        transaction.setNonPayable(amount + abs(restForPayable)); // rest may goes negative
                    }
                } else if (txPayable + nonPayable >= 0) {
                    // try to set nonPayable to 0 and put rest on payable
                    transaction.setNonPayable(-nonPayable);
                    transaction.setPayable(txPayable + nonPayable + txNonPayable);
                } else if (txPayable + txNonPayable > 0) {
                    // try to balance non payable
                    long newTxPayable = nonPayable + txNonPayable + txPayable;
                    long transactionAmount = txPayable + txNonPayable;
                    // must be >= 0
                    newTxPayable = max(newTxPayable, 0);
                    // can not be more than initial transaction-payable amount
                    newTxPayable = min(newTxPayable, txPayable);
                    transaction.setNonPayable(transactionAmount - newTxPayable);
                    transaction.setPayable(newTxPayable);
                }
            }
            // finally minus account only allowed in case of rollbacks / cancellations / chargebacks
        }

        if (txAmount != transaction.getAmount()) {
            throw new RuntimeException("Broken amount calculation");
        }

        payable += transaction.getPayable();
        nonPayable += transaction.getNonPayable();

        transaction.setPostBalance(getBalance());
        logger.exiting(Account.class.getName(), "changeBalance");
    }

    public long getBalance() {
        return payable + nonPayable;
    }

    public long getPayable() {
        return payable;
    }

    public long getNonPayable() {
        return nonPayable;
    }

    public Player getPlayer() {
        return player;
    }

    public String getPlayerReference() {
        return playerReference;
    }

    public static Account findByPlayerId(EntityManager entityManager, String playerId) throws NoResultException {
        TypedQuery<Account> query = entityManager.createNamedQuery("findByPlayerId", Account.class);
        query.setParameter("playerId", playerId);
        try {
            return query.getSingleResult();
        } catch (NonUniqueResultException | NoResultException e) {
            return null;
        }
    }

}
