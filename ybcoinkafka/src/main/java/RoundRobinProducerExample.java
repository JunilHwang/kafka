import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.Partitioner;
import kafka.producer.ProducerConfig;
import kafka.utils.VerifiableProperties;

public class RoundRobinProducerExample {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092,localhost:9093,localhost:9094");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", RoundRobinPartitioner.class.getName());
        ProducerConfig producerConfig = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(producerConfig);
        KeyedMessage<String, String> message = new KeyedMessage<String, String>("test", "", "Hello, World!");
        producer.send(message);
        producer.close();
    }

    public static class RoundRobinPartitioner implements Partitioner {
        private AtomicInteger n = new AtomicInteger(0);

        public RoundRobinPartitioner(VerifiableProperties props) {}

        @Override
        public int partition(Object key, int numPartitions) {
            int i = n.getAndIncrement();
            if (i == Integer.MAX_VALUE) {
                n.set(0);
                return 0;
            }
            return i % numPartitions;
        }
    }
}