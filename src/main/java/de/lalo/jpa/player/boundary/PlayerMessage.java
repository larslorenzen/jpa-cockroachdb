package de.lalo.jpa.player.boundary;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author llorenzen
 * @since 30.12.17
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerMessage {

    private String type; // CREATED, CHANGED

    private String playerId;

    private String currency;

    private String email;

    private String firstname;

    private String lastname;

    public String getPlayerId() {
        return playerId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
