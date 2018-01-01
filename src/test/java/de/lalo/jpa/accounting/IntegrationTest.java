package de.lalo.jpa.accounting;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author llorenzen
 * @since 30.12.17
 */
public class IntegrationTest {


    private final static String TOPIC = "transactions";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private KafkaConsumerRunner runner;

    @Test
    public void testCreateTransactions() throws ExecutionException, InterruptedException {
        final Producer<String, String> producer = createProducer();
        long time = System.currentTimeMillis();
        consumeResponses();

        try {
            for (long index = 0; index < 2; index++) {

                long id = 123456 + index;

                String json = "{\"id\": \"" + id + "\", \"playerId\": \"aabbccc\", \"payable\": 0, \"nonPayable\": " + (index + 1) * 100 + ", \"description\": \"just a test\"}";

                ProducerRecord<String, String> record =
                        new ProducerRecord<>(TOPIC, "aabbccc", json);

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
        Thread.sleep(60000);
        runner.shutdown();
    }

    public void consumeResponses() {
        KafkaConsumer consumer = createConsumer();
        runner = new KafkaConsumerRunner(consumer);
        Thread thread = new Thread(runner);
        thread.start();
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
