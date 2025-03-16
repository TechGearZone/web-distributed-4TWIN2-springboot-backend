package com.esprit.ms.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator getawayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                //nom de l'app ds app.propreties
                .route("MSCandidat4TWIN2",r->r.path("/candidats/**") //tous les path sous candidats
                        .uri("lb://MSCandidat4TWIN2"))//port candidat=8080
                .route("MS-job-s",r->r.path("/jobs/**")
                        .uri("lb://MS-job-s"))

                .build();
    }
}
