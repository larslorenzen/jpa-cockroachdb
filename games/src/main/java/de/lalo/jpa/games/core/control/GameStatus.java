package de.lalo.jpa.games.core.control;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author llorenzen
 * @since 12.01.18
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GameStatus {

    private long balance;

    private boolean success;

    public GameStatus(long balance, boolean success) {
        this.balance = balance;
        this.success = success;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
