package com.example.springkafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Configuration
public class KafkaSender {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value(value = "${spring.kafka.template.default-topic}")
    private String topic;

    @Bean
    public Map<String, Object> producerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Course> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, Course> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    @PostMapping("/send")
    public void sender(@RequestBody Map<String, Object> param){
        log.info("sending to kafka : {}", param);
        int id = (int) param.get("id");
        String name = (String) param.get("name");
        String professor = (String) param.get("professor");
        kafkaTemplate().send(topic, new Course(id, name, professor));
    }
}
