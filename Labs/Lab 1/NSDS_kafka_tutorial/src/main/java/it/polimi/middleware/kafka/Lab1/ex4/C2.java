package it.polimi.middleware.kafka.Lab1.ex4;



import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class C2 {
    private static final String defaultConsumerGroupId = "groupB";
    private static final String defaultInputTopic = "topicA";
    private static final String defaultOutputTopic = "topicB";

    private static final String serverAddr = "localhost:9092";
    private static final String producerTransactionalId = "forwarderTransactionalId";

    // Default is "latest": try "earliest" instead
    private static final String offsetResetStrategy = "latest";  //start from the latest published topic

    public static void main(String[] args) {
        int keycount[] = new int[1000];

        // If there are arguments, use the first as group and the second as topic.
        // Otherwise, use default group and topic.
        String groupId = args.length >= 1 ? args[0] : defaultConsumerGroupId;
        String topic = args.length >= 2 ? args[1] : defaultInputTopic;
        String outputTopic = args.length >= 3 ? args[2] : defaultOutputTopic;

        // Consumer
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddr);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetResetStrategy);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        // The consumer does not commit automatically, but within the producer transaction
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(false));
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        // Producer
        final Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddr);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, producerTransactionalId);
        producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, String.valueOf(true));
        // Idempotence = exactly once semantics between the producer and the partition

        final KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);
        producer.initTransactions();


        while (true) {
            final ConsumerRecords<String, String> records = consumer.poll(Duration.of(5, ChronoUnit.MINUTES));
            producer.beginTransaction();
            for (final ConsumerRecord<String, String> record : records) {

                String subs = record.key().substring(3, record.key().length());
                System.out.println(subs);
                int keyval = Integer.parseInt(subs);
                keycount[keyval] ++;
                int count = keycount[keyval];
                System.out.print("Consumer group: " + groupId + "\t");
                System.out.println("Partition: " + record.partition() +
                        "\tOffset: " + record.offset() +
                        "\tKey: " + record.key() +
                        "\tValue: " + record.value()
                );
                producer.send(new ProducerRecord<>(outputTopic, record.key() + "_topicB", record.value() + ", key count: " + count));
            }

            // The producer manually commits the offsets for the consumer within the transaction
            final Map<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
            for (final TopicPartition partition : records.partitions()) {
                final List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                final long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                map.put(partition, new OffsetAndMetadata(lastOffset + 1));
            }

            producer.sendOffsetsToTransaction(map, consumer.groupMetadata());
            producer.commitTransaction();
        }
    }
}
