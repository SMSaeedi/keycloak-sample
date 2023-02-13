package com.qrebl.users.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
public class KeycloakConfig {
    static Keycloak keycloak = null;
    final static String client_id = "admin-panel";
    final static String admin_client_id = "admin-cli";
    final static String username = "user";
    final static String admin_username = "admin";
    final static String admin_password = "admin";
    final static String password = "user123";
    final static String grant_type = "password";
    public final static String realm = "user-realm";
    public final static String realm_admin = "master";
    final static String client_secret = "nOHDjssKScrKtLRgLlxrB2plwnM3ym3A";
    private static ResteasyClient resteasyClient;

    @Value("${keycloak.admin-panel.url}")
    private static String url="http://192.168.0.136:8180/auth";

    @PostConstruct
    public void init(){
        resteasyClient = getResteasyClient();
    }

    public static Keycloak getInstance(Config config) {
        return KeycloakBuilder.builder()
                .serverUrl(url)
                .grantType(grant_type)
                .username(config.getUsername())
                .password(config.getPassword())
                .realm(config.getRealm())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .resteasyClient(resteasyClient)
                .build();
    }

    public static Keycloak getInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(url)
                    .grantType(grant_type)
                    .username(admin_username)
                    .password(admin_password)
                    .realm(realm_admin)
                    .clientId(admin_client_id)
                    .clientSecret(client_secret)
                    .resteasyClient(resteasyClient)
                    .build();
        }
        return keycloak;
    }

    public static ResteasyClient getResteasyClient() {
        return new ResteasyClientBuilder()
                .connectionPoolSize(10)
                .build();
    }

    public static Config newConfig() {
        return new Config(url, realm_admin, null, null,admin_client_id, null);
    }
}