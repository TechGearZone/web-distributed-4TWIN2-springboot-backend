package com.esprit.ms.api_gateway_4twin2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGateway4Twin2Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiGateway4Twin2Application.class, args);
	}

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("Delivery", r -> r.path("/api/deliveries/**")
						.uri("lb://Delivery"))
				.route("Driver", r -> r.path("/api/drivers/**")
						.uri("lb://Delivery"))
				.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		// Allow your frontend origin (adjust based on your frontend's port)
		config.addAllowedOrigin("http://localhost:8080"); // Example: if using live-server
		config.addAllowedOrigin("http://localhost:4200"); // Example: if using React
		config.addAllowedOrigin("null"); // Temporarily allow null for testing (remove in production)
		config.addAllowedMethod("*"); // Allow GET, OPTIONS, etc.
		config.addAllowedHeader("*"); // Allow all headers (e.g., Authorization)
		config.setAllowCredentials(true); // If credentials are needed
		config.setMaxAge(3600L); // Cache pre-flight response for 1 hour

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config); // Apply to all paths
		return source;
	}
}