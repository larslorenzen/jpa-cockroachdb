package de.lalo.jpa.games.accounting.control;

/**
 * @author llorenzen
 * @since 12.01.18
 */
public class TransactionEvent {
    private final String playerId;
    private final long stake;

    public TransactionEvent(String playerId, long stake) {
        this.playerId = playerId;
        this.stake = stake;
    }

    public String getPlayerId() {
        return playerId;
    }

    public long getStake() {
        return stake;
    }
}
