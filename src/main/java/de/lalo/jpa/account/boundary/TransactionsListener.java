package de.lalo.jpa.account.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.lalo.jpa.account.control.AccountingService;
import fish.payara.cloud.connectors.kafka.api.KafkaListener;
import fish.payara.cloud.connectors.kafka.api.OnRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.io.IOException;

/**
 * @author llorenzen
 * @since 26.12.17
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "accounting"),
        @ActivationConfigProperty(propertyName = "groupIdConfig", propertyValue = "accounting-transactions"),
        @ActivationConfigProperty(propertyName = "topics", propertyValue = "transactions"),
        @ActivationConfigProperty(propertyName = "bootstrapServersConfig", propertyValue = "${kafka.servers}"),
        @ActivationConfigProperty(propertyName = "autoCommitInterval", propertyValue = "100"),
        @ActivationConfigProperty(propertyName = "retryBackoff", propertyValue = "1000"),
        @ActivationConfigProperty(propertyName = "keyDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),
        @ActivationConfigProperty(propertyName = "valueDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),
        @ActivationConfigProperty(propertyName = "pollInterval", propertyValue = "1000"),
})
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
public class TransactionsListener implements KafkaListener {

    @Inject
    private AccountingService accountingService;

    @OnRecord(topics = {"transactions"})
    public void addTransaction(ConsumerRecord record) {
        System.out.println("Got record on thread " + Thread.currentThread().getId() + " " + record);
        Object value = record.value();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TransactionMessage transactionMessage = objectMapper.readValue(value.toString(), TransactionMessage.class);
            accountingService.performTransaction(transactionMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
