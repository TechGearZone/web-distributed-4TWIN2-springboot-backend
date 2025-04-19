package com.esprit.microservice.Delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class DeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}

	@Autowired
	private DeliveryRepository deliveryRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Bean
	ApplicationRunner init() {
		return (args) -> {

		};
	}
}
