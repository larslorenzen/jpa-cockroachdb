package de.lalo.jpa.games;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author llorenzen
 * @since 30.12.17
 */
@Ignore
public class IntegrationTest {

    static final int NUM_MESSAGES = 400;

    private final static String TOPIC = "transactions";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private KafkaConsumerRunner runner;
    private Thread consumerThread;

    @Test
    public void testCreateTransactions() throws ExecutionException, InterruptedException {
        RestIntegrationTest restIntegrationTest = new RestIntegrationTest();
        restIntegrationTest.createTestPlayers();

        final Producer<String, String> producer = createProducer();
        long time = System.currentTimeMillis();
        consumeResponses();

        try {
            for (int index = 0; index < NUM_MESSAGES; index++) {

                String playerId = restIntegrationTest.selectPlayer(index);
                String json = restIntegrationTest.transaction(index, playerId);

                ProducerRecord<String, String> record =
                        new ProducerRecord<>(TOPIC, playerId, json);

                RecordMetadata metadata = producer.send(record).get();

                long elapsedTime = System.currentTimeMillis() - time;
                System.out.printf("sent record(key=%s value=%s) " +
                                "meta(partition=%d, offset=%d) time=%d\n",
                        record.key(), record.value(), metadata.partition(),
                        metadata.offset(), elapsedTime);
            }
        } finally {
            producer.flush();
            producer.close();
        }
        consumerThread.join();
        runner.shutdown();
    }

    public void consumeResponses() {
        KafkaConsumer consumer = createConsumer();
        runner = new KafkaConsumerRunner(consumer);
        consumerThread = new Thread(runner);
        consumerThread.start();
    }

    private static KafkaConsumer createConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<>(props);
    }

    private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

}
