package ybcoin.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class HomeController {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    KafkaTemplate kafkaTemplate;

    @GetMapping(value="/get/{key}")
    public String setData(){
        return "success";
    }

    @GetMapping(value="/set/{topic}/{key}/{value}")
    public String getData(@PathVariable("topic") String topic,
                          @PathVariable("key") String key,
                          @PathVariable("value") String value){
        kafkaTemplate.send(topic, key, value);
        return String.format("%s > %s : %s", topic, key, value);
    }
}
