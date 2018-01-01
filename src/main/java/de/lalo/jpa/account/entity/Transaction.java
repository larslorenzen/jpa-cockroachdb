package de.lalo.jpa.account.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

import static de.lalo.jpa.account.entity.TransactionState.*;

/**
 * @author llorenzen
 * @since 23.10.17
 */
@Entity
@Table(name = "transactions", indexes = @Index(columnList = "account_id"))
@NamedQueries({
        @NamedQuery(name = "findByAccount", query = "SELECT t FROM Transaction  t WHERE t.account = :account")
})
public class Transaction implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private long payable;

    private long nonPayable;

    private String description;

    private String reference;

    private TransactionState state = TransactionState.PENDING;

    private Date crDate;

    private Date bookingDate;

    private Date cancelDate;

    private long preBalance;

    private long postBalance;

    public Transaction() {
        // for JPA
    }

    public Transaction(Account account, String reference, long payable, long nonPayable, String description) {
        this.payable = payable;
        this.nonPayable = nonPayable;
        this.account = account;
        this.reference = reference;
        this.description = description;
    }

    public void perform() {
        getAccount().perform(this);
        bookingDate = new Date();
        if (crDate == null) {
            crDate = bookingDate;
        }
        changeState(PERFORMED);
    }

    protected boolean changeState(TransactionState to) {
        if (this.state == to) {
            return false;
        }
        if (isTransitionOk(this.state, to)) {
            this.state = to;
            return true;
        } else {
            throw new IllegalStateException("Transaction state is not allowed: " + this.state + " for transaction with id: " + id);
        }
    }


    public long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public long getPayable() {
        return payable;
    }

    public long getNonPayable() {
        return nonPayable;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }

    public TransactionState getState() {
        return state;
    }

    public Date getCrDate() {
        return crDate;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public long getPreBalance() {
        return preBalance;
    }

    public long getPostBalance() {
        return postBalance;
    }

    public void setPreBalance(long preBalance) {
        this.preBalance = preBalance;
    }

    public long getAmount() {
        return payable + nonPayable;
    }

    public void setNonPayable(long nonPayable) {
        this.nonPayable = nonPayable;
    }

    public void setPayable(long payable) {
        this.payable = payable;
    }

    public void setPostBalance(long postBalance) {
        this.postBalance = postBalance;
    }

}
