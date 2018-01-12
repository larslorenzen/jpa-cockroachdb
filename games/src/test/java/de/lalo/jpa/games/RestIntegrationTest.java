package de.lalo.jpa.games;

import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.util.UUID;

/**
 * @author llorenzen
 * @since 09.01.18
 */
@Ignore
public class RestIntegrationTest {

    private final Client client;

    public static void main(String[] args) {
        new RestIntegrationTest().testTransactions();
    }

    public RestIntegrationTest() {
        client = ClientBuilder.newClient();
    }

    @Test
    public void testTransactions() {
//        createTestPlayers();

        for (int i = 0; i < IntegrationTest.NUM_MESSAGES; i++) {
            String playerId = selectPlayer(i);
            depositMoney(i, playerId);
        }
    }

    private void depositMoney(int index, String playerId) {
        api().path("/games/play")
                .request().header("playerId", playerId)
                .post(javax.ws.rs.client.Entity.json(transaction(index, playerId)));
    }

    String selectPlayer(int index) {
        String key;
        int indexMod = index % 4;
        switch (indexMod) {
            case 0:
                key = "jdo";
                break;
            case 1:
                key = "aabbccc";
                break;
            case 2:
                key = "ThePlayer";
                break;
            default:
                key = UUID.randomUUID().toString();
        }
        return key;
    }

    public void createTestPlayers() {
        String player = player("aabbccc", "lalo@example.com", "Max", "Mustermann");
        createPlayer(player);

        player = player("jdo", "john.doe@example.com", "John", "Doe");
        createPlayer(player);

        player = player("ThePlayer", "The.player@example.com", "The", "Player");
        createPlayer(player);
    }

    private void createPlayer(String player) {
        api().path("/players").request().post(Entity.json(player));
    }

    private WebTarget api() {
        return client.target("http://localhost:8081/api");
    }

    String transaction(int index, String playerId) {
        long id = 123456 + index;
        return "{\"id\": \"" + id + "\", \"playerId\": \"" + playerId + "\", \"payable\": 0, \"nonPayable\": " + (index + 1) * 100 + ", \"description\": \"just a test\"}";
    }

    private String player(String id, String email, String firstname, String lastname) {
        return "{\"type\":\"CREATED\", \"playerId\":\"" + id + "\", \"currency\": \"EUR\", \"email\": \"" + email + "\", \"firstname\":\"" + firstname + "\", \"lastname\": \"" + lastname + "\"}";
    }
}
