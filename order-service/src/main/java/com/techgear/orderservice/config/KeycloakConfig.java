/*
package com.techgear.orderservice.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import
        org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientImpl;
import org.keycloak.OAuth2Constants;
import
        org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import
        org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import
        org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class KeycloakConfig {
    @Bean
    public KeycloakSpringBootConfigResolver
    keycloakSpringBootConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
    static Keycloak keycloak=null;
    final static String serverUrl = "http://localhost:8080/auth";
    public final static String realm = "TeckGear";
    public final static String clientId = "order-service";
    final static String clientSecret =
            "yHArSL0W7GEZlevVnuOfa7pvy6vlHT0b";
    final static String userName = "admin1";
    final static String password = "admin";
    public KeycloakConfig() {
    }
    @Bean
    public static Keycloak getInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilderImpl()
                            .connectionPoolSize(10)
                            .build())
                    .build();
        }
        return keycloak;
    }
}*/
