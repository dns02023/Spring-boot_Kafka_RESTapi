package com.example.springkafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.*;

@Slf4j
@EnableKafka
@RestController
@Configuration
public class KafkaReceiver {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value(value = "${spring.kafka.template.default-topic}")
    private String topic;


    @Bean
    public Map<String, Object> consumerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,topic+"-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @GetMapping("/consumer")
    public void consumer(){
        List<Map<String, String>> list;
        Map<String, String> map;

        KafkaConsumer kafkaConsumer = new KafkaConsumer(consumerConfig());
        kafkaConsumer.subscribe(Arrays.asList(topic));
        while (true){
            list = new ArrayList<>();
            map = new HashMap<>();
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(10));
            for (ConsumerRecord<String, String> record : records){
                if(record.value()!=null){
                    map.put(String.valueOf(record.value().hashCode()), record.value());
                }
            }
            list.add(map);
            System.out.println(list);
        }

    }




}
