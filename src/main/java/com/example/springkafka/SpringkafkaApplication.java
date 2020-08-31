package com.example.springkafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringkafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringkafkaApplication.class, args);
	}

}
