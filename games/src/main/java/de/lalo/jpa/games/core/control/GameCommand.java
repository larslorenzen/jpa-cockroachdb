package de.lalo.jpa.games.core.control;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author llorenzen
 * @since 12.01.18
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GameCommand {

    private String gameName;

    private String playerId;

    private long stake;


    public String getGameName() {
        return gameName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public long getStake() {
        return stake;
    }
}
