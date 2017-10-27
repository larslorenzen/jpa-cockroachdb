package de.lalo.jpa.player;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

/**
 * @author llorenzen
 * @since 21.10.17
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class Particulars implements Serializable {

    private String firstname;

    private String lastname;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
