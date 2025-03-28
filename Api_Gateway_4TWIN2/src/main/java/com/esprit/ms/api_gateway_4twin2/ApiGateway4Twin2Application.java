package com.esprit.ms.api_gateway_4twin2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGateway4Twin2Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiGateway4Twin2Application.class, args);
	}

	@Bean
	public RouteLocator getwayRoutes(RouteLocatorBuilder builder)
	{

		return builder.routes()
				.route("Delivery", r -> r.path("/api/deliveries/**") // Modifier le chemin ici
						.uri("lb://Delivery"))

				.route("Driver", r -> r.path("/api/drivers/**")
						.uri("lb://Delivery"))
				.build();
	}
}