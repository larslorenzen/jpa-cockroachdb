package de.lalo.jpa.player.boundary;

import de.lalo.jpa.player.control.PlayerService;
import de.lalo.jpa.player.entity.Player;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author llorenzen
 * @since 21.10.17
 */
@Stateless
@Path("players")
public class PlayersResource {

    @PersistenceContext(unitName = "lotto")
    private EntityManager entityManager;

    @Inject
    private PlayerService playerService;

    @GET
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Player> getPlayers() {
        Query query = entityManager.createNamedQuery("findAll");
        return query.getResultList();
    }

    @POST
    public Response createPlayer(PlayerMessage player) {
        playerService.registered(player);
        return Response.ok().build();
    }

}
