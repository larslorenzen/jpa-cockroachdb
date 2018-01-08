package de.lalo.jpa.player.control;

import de.lalo.jpa.account.control.AccountingService;
import de.lalo.jpa.player.boundary.PlayerMessage;
import de.lalo.jpa.player.entity.Player;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author llorenzen
 * @since 30.12.17
 */
@Stateless
public class PlayerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private AccountingService accountingService;

    public void registered(PlayerMessage playerMessage) {
        Player player = new Player();
        player.setEmail(playerMessage.getEmail());
        player.setPlayerReference(playerMessage.getPlayerId());
        player.getParticulars().setFirstname(playerMessage.getFirstname());
        player.getParticulars().setLastname(playerMessage.getLastname());
        entityManager.persist(player);
        accountingService.createAccount(player, playerMessage.getCurrency());
    }

}
