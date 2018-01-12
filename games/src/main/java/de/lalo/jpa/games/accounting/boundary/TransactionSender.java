package de.lalo.jpa.games.accounting.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lalo.jpa.games.accounting.control.TransactionEvent;
import fish.payara.cloud.connectors.kafka.api.KafkaConnection;
import fish.payara.cloud.connectors.kafka.api.KafkaConnectionFactory;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.resource.ConnectionFactoryDefinition;
import java.util.UUID;

/**
 * @author llorenzen
 * @since 12.01.18
 */
@ConnectionFactoryDefinition(name = "java:module/env/KafkaConnectionFactory",
        description = "Kafka Connection Factory",
        interfaceName = "fish.payara.cloud.connectors.kafka.KafkaConnectionFactory",
        resourceAdapter = "kafka-rar",
        minPoolSize = 2,
        maxPoolSize = 2,
        properties = {
                "bootstrapServersConfig=localhost:9092",
                "clientId=Games"
        })
@Stateless
public class TransactionSender {

    @Resource(lookup = "java:module/env/KafkaConnectionFactory")
    KafkaConnectionFactory factory;

    public void sendTransaction(@Observes TransactionEvent event) {

        String id = UUID.randomUUID().toString();

        TransactionMessage message = new TransactionMessage(id, event.getPlayerId(), -event.getStake(), "For the games");

        try (KafkaConnection connection = factory.createConnection()) {
            connection.send(new ProducerRecord<>("transactions", event.getPlayerId(), toJsonString(message)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String toJsonString(Object message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(message);
    }
}
