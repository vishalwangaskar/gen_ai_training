package com.epam.training.gen.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
//@PropertySource("classpath:/config/application.properties")
public class GenAiTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenAiTrainingApplication.class, args);
	}

}
