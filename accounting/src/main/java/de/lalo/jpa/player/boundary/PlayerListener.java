package de.lalo.jpa.player.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.lalo.jpa.player.control.PlayerService;
import fish.payara.cloud.connectors.kafka.api.KafkaListener;
import fish.payara.cloud.connectors.kafka.api.OnRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import java.io.IOException;

/**
 * @author llorenzen
 * @since 30.12.17
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "accounting"),
        @ActivationConfigProperty(propertyName = "groupIdConfig", propertyValue = "accounting-players"),
        @ActivationConfigProperty(propertyName = "topics", propertyValue = "players"),
        @ActivationConfigProperty(propertyName = "bootstrapServersConfig", propertyValue = "${kafka.servers}"),
        @ActivationConfigProperty(propertyName = "autoCommitInterval", propertyValue = "100"),
        @ActivationConfigProperty(propertyName = "retryBackoff", propertyValue = "1000"),
        @ActivationConfigProperty(propertyName = "keyDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),
        @ActivationConfigProperty(propertyName = "valueDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),
        @ActivationConfigProperty(propertyName = "pollInterval", propertyValue = "1000"),
})
public class PlayerListener implements KafkaListener {

    @Inject
    private PlayerService playerService;

    @OnRecord(topics = "players")
    public void playerChanged(ConsumerRecord record) {
        Object value = record.value();
        try {
            PlayerMessage playerMessage = new ObjectMapper().readValue(value.toString(), PlayerMessage.class);
            playerService.registered(playerMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
