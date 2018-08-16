package kafkaspringtutorial.demo;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:kafka.properties")
@EnableKafka
public class KafkaConfiguration {

    @Autowired
    private Environment env;

    public Map<String, Object> producerConfigs() {

        Map<String, Object> props = new HashMap<>();

        // server host 및 port 지정
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));

        // retries 횟수
        props.put(ProducerConfig.RETRIES_CONFIG, env.getProperty(ProducerConfig.RETRIES_CONFIG));

        // batch size 지정
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, env.getProperty(ProducerConfig.BATCH_SIZE_CONFIG));

        // linger.ms
        props.put(ProducerConfig.LINGER_MS_CONFIG, env.getProperty(ProducerConfig.LINGER_MS_CONFIG));

        // buffer memory size 지정
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, env.getProperty(ProducerConfig.BUFFER_MEMORY_CONFIG));

        // key serialize 지정
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // value serialize 지정
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }


    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        // Bean을 통하여 의존성 주입
        return new KafkaTemplate<String, String>(producerFactory());
    }

}
