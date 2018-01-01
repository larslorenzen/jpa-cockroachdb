package de.lalo.jpa.account.boundary;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author llorenzen
 * @since 29.12.17
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TransactionMessage {

    private String id;

    private String playerId;

    private long payable;

    private long nonPayable;

    private String description;

    public long getPayable() {
        return payable;
    }

    public long getNonPayable() {
        return nonPayable;
    }

    public String getDescription() {
        return description;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TransactionMessage{" +
                "id='" + id + '\'' +
                ", playerId='" + playerId + '\'' +
                ", payable=" + payable +
                ", nonPayable=" + nonPayable +
                ", description='" + description + '\'' +
                '}';
    }
}
