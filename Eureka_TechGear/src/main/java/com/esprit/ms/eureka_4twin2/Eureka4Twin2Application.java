package com.esprit.ms.eureka_4twin2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Eureka4Twin2Application {

	public static void main(String[] args) {
		SpringApplication.run(Eureka4Twin2Application.class, args);
	}

}
