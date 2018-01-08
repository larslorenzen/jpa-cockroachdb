package de.lalo.jpa.player.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

/**
 * @author llorenzen
 * @since 21.10.17
 */
@Entity
@Table(name = "players", indexes = {@Index(name = "emailUnique", columnList = "email", unique = true)})
@NamedQueries({@NamedQuery(name = "findAll", query = "SELECT p FROM Player p")})
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String playerReference;

    private String email;

    private String state = "REGISTERED";

    private Particulars particulars;

    public Particulars getParticulars() {
        if (particulars == null) {
            particulars = new Particulars();
        }
        return particulars;
    }

    public String getState() {
        return state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setPlayerReference(String playerId) {
        this.playerReference = playerId;
    }

    public String getPlayerReference() {
        return playerReference;
    }
}
