package de.lalo.jpa.games.core.boundary;

import de.lalo.jpa.games.core.control.GameCommand;
import de.lalo.jpa.games.core.control.GameStatus;
import de.lalo.jpa.games.core.control.GamingService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author llorenzen
 * @since 12.01.18
 */
@Stateless
@Path("/games")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GamingResource {

    @Inject
    private GamingService gamingService;

    /**
     * provided by the login manager
     * (filter, gateway or something else. Might also be included in the game command)
     */
    @HeaderParam("playerId")
    private String playerId;

    @POST
    @Path("/play")
    public Response playGame(GameCommand gameCommand) {
        GameStatus gameStatus = gamingService.playGame(gameCommand);
        return Response.ok(gameStatus).build();
    }

}
