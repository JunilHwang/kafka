import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class ConsumerExample {

    private static final String TOPIC = "test";
    private static final int NUM_THREADS = 20;

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("group.id", "test-group");
        props.put("zookeeper.connect", "localhost:2181");
        props.put("auto.commit.interval.ms", "1000");
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, NUM_THREADS);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(TOPIC);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    for (MessageAndMetadata<byte[], byte[]> messageAndMetadata : stream) {
                        System.out.println(new String(messageAndMetadata.message()));
                    }
                }
            });
        }
        Thread.sleep(60000);
        consumer.shutdown();
        executor.shutdown();
    }
}