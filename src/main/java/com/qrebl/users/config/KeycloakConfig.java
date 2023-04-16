package com.qrebl.users.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class KeycloakConfig {
    static Keycloak keycloak = null;
    final static String client_id = "admin-cli";
    final static String admin_client_id = "admin-cli";
    final static String username = "user";
    final static String admin_username = "admin";
    final static String admin_password = "admin";
    final static String password = "admin";
    final static String grant_type = "password";
    public final static String realm = "master";
    public final static String realm_admin = "master";
    final static String client_secret = "01z88W8qRNZFo8UkFtslEn6JUeKjQ0eQ";
    //    final static String client_secret = "EroAp6Ex4qz848LqUYxU5sPtpyQX5iPv";
    private static ResteasyClient resteasyClient;
    private static String url = "http://172.18.1.177:1443/auth";

    @PostConstruct
    public void init() {
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
                    .realm(realm_admin)
                    .grantType(grant_type)
                    .username(admin_username)
                    .password(password)
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
        return new Config(url, realm_admin, admin_username, password, client_id, client_secret);
    }
}