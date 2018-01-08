package de.lalo.jpa.account.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.lalo.jpa.account.control.AccountingService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author llorenzen
 * @since 26.12.17
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TransactionsListener {

    @Inject
    private AccountingService accountingService;

    @Resource(lookup = "concurrent/transactions")
    private ManagedExecutorService executorService;

    private List<KafkaPoller> pollers = new ArrayList<>();

    @PostConstruct
    void postConstruct() {
        for (int i = 0; i < 10; i++) {
            KafkaPoller poller = new KafkaPoller(accountingService, i);
            executorService.execute(poller);
            pollers.add(poller);
        }
    }

    private static KafkaConsumer<String, String> createConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "accounting-transactions");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "100");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<>(props);
    }

    private static class KafkaPoller implements Runnable {

        private AtomicBoolean closed = new AtomicBoolean();

        private KafkaConsumer<String, String> consumer;

        private AccountingService accountingService;

        private final int nr;

        KafkaPoller(AccountingService accountingService, int nr) {
            this.accountingService = accountingService;
            this.nr = nr;
            consumer = createConsumer();
        }

        @Override
        public void run() {
            try {
                consumer.subscribe(Arrays.asList("transactions"));
                while (!closed.get()) {
                    ConsumerRecords<String, String> records = consumer.poll(1000);
                    // Handle new records
                    for (ConsumerRecord<String, String> record : records) {
                        addTransaction(record);
                    }
                }
            } catch (WakeupException e) {
                // Ignore exception if closing
                if (!closed.get()) throw e;
            } finally {
                consumer.close();
            }
        }

        public void addTransaction(ConsumerRecord record) {
            System.out.println("Got record on thread " + nr + " " + record);
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

}
