package de.lalo.jpa.games.accounting.boundary;

import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * @author llorenzen
 * @since 12.01.18
 */
@Stateless
public class AccountStatusClient {

    public Long getCurrentBalance(String playerId) {
        Client client = ClientBuilder.newClient();
        try {
            JsonObject accountResponse = client.target("http://localhost:8080/api/accounts/" + playerId)
                    .request().get(JsonObject.class);
            return accountResponse.getJsonNumber("balance").longValue();
        } catch (NotFoundException e) {
            return null;
        }
    }

}
