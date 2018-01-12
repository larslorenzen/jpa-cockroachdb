package de.lalo.jpa.games.accounting.entity;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

/**
 * @author llorenzen
 * @since 12.01.18
 */
@Entity
@Table(name = "accounts")
@NamedQueries({
        @NamedQuery(name = "findByPlayerId", query = "SELECT a FROM Account a WHERE a.playerId = :playerId")
})
public class Account {

    @Id
    @GeneratedValue
    private long id;

    private String playerId;

    private long balance;

    public Account() {
        // for jpa
    }

    public Account(String playerId, long currentBalance) {
        this.playerId = playerId;
        this.balance = currentBalance;
    }

    public String getPlayerId() {
        return playerId;
    }

    public long getBalance() {
        return balance;
    }

    public static Account findByPlayerId(EntityManager entityManager, String playerId) {
        TypedQuery<Account> query = entityManager.createNamedQuery("findByPlayerId", Account.class);
        query.setParameter("playerId", playerId);
        return query.getSingleResult();
    }

    public void reduceBalance(long stake) {
        balance -= stake;
    }

    public void updateBalance(long balance) {
        this.balance  = balance;
    }
}
