package de.lalo.jpa.games.accounting.boundary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * // {"id":"305","reference":"123848","preBalance":1911000,"postBalance":1950300,"amount":39300,"state":"success"}
 * @author llorenzen
 * @since 12.01.18
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionChangeRecord {

    private String id;

    private String reference;

    private String playerId;

    private long postBalance;

    private String state;

    public String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public long getPostBalance() {
        return postBalance;
    }

    public String getState() {
        return state;
    }

    public String getPlayerId() {
        return playerId;
    }
}
