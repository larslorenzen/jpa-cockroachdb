package de.lalo.jpa.account.boundary;

import de.lalo.jpa.account.entity.Account;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author llorenzen
 * @since 12.01.18
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountResponse {

    private long balance;

    public AccountResponse(Account account) {
        this.balance = account.getBalance();
    }

    public long getBalance() {
        return balance;
    }
}
