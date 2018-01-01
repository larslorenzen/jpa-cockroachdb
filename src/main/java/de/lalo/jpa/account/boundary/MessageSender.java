package de.lalo.jpa.account.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lalo.jpa.account.entity.Transaction;
import fish.payara.cloud.connectors.kafka.api.KafkaConnection;
import fish.payara.cloud.connectors.kafka.api.KafkaConnectionFactory;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.resource.ConnectionFactoryDefinition;

/**
 * @author llorenzen
 * @since 28.12.17
 */
@ConnectionFactoryDefinition(name = "java:module/env/KafkaConnectionFactory",
        description = "Kafka Connection Factory",
        interfaceName = "fish.payara.cloud.connectors.kafka.KafkaConnectionFactory",
        resourceAdapter = "kafka-rar",
        minPoolSize = 2,
        maxPoolSize = 2,
        properties = {
                "bootstrapServersConfig=localhost:9092",
                "clientId=Lotto"
        })
@Stateless
public class MessageSender {

    @Resource(lookup = "java:module/env/KafkaConnectionFactory")
    KafkaConnectionFactory factory;

    public void transactionChanged(@Observes TransactionChangedEvent event) {
        Transaction transaction = event.getTransaction();

        TransactionChangedMessage message = new TransactionChangedMessage(transaction.getId() + "", transaction.getReference(),
                transaction.getPreBalance(), transaction.getPostBalance(), transaction.getAmount(), "success");

        try (KafkaConnection connection = factory.createConnection()) {
            connection.send(new ProducerRecord<>("transactionChanges", transaction.getAccount().getPlayerReference(), toJsonString(message)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String toJsonString(TransactionChangedMessage message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(message);
    }
}
