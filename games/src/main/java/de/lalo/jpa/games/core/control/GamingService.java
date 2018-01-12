package de.lalo.jpa.games.core.control;

import de.lalo.jpa.games.accounting.control.AccountUpdateManager;
import de.lalo.jpa.games.accounting.control.BookingResult;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author llorenzen
 * @since 12.01.18
 */
@Stateless
public class GamingService {

    @Inject
    private AccountUpdateManager accountUpdateManager;

    public GameStatus playGame(GameCommand gameCommand) {
        BookingResult bookingResult = accountUpdateManager.performBookingOf(gameCommand.getPlayerId(), gameCommand.getStake());
        return new GameStatus(bookingResult.getBalance(), bookingResult.isSuccess());
    }
}
