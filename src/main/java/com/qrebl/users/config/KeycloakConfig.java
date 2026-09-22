package com.qrebl.users.config;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    public static final String realm = "master";
    private static final String GRANT_TYPE = "password";
    private Keycloak keycloak;
    @Getter
    private final Client resteasyClient;

    @Value("${keycloak.server-url}")
    private String serverUrl;
    @Getter
    @Value("${keycloak.realm:master}")
    private String configuredRealm;
    @Value("${keycloak.admin-username}")
    private String adminUsername;
    @Value("${keycloak.admin-password}")
    private String adminPassword;
    @Value("${keycloak.client-id:admin-cli}")
    private String clientId;
    @Value("${keycloak.client-secret:}")
    private String clientSecret;

    public KeycloakConfig() {
        resteasyClient = ClientBuilder.newBuilder().build();
    }

    public Keycloak getInstance(Config config) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .grantType(GRANT_TYPE)
                .username(config.getUsername())
                .password(config.getPassword())
                .realm(config.getRealm())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .resteasyClient(resteasyClient)
                .build();
    }

    public synchronized Keycloak getInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(configuredRealm)
                    .grantType(GRANT_TYPE)
                    .username(adminUsername)
                    .password(adminPassword)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(resteasyClient)
                    .build();
        }
        return keycloak;
    }

    public Config newConfig() {
        return new Config(serverUrl, configuredRealm, adminUsername, adminPassword, clientId, clientSecret);
    }

    public Config newConfig(String realm, String username, String password, String requestedClientId,
                            String requestedClientSecret) {
        return new Config(serverUrl, realm, username, password, requestedClientId, requestedClientSecret);
    }

    @PreDestroy
    public void close() {
        if (keycloak != null) {
            keycloak.close();
        }
        resteasyClient.close();
    }
}