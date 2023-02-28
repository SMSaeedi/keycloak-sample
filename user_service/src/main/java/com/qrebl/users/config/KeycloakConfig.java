package com.qrebl.users.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Configuration
public class KeycloakConfig {
    static Keycloak keycloak = null;
    final static String admin_client_id = "admin-cli";
    final static String admin_username = "admin";
    final static String admin_password = "admin";
    final static String grant_type = "password";
    public final static String realm = "user-realm";
    public final static String realm_admin = "master";
    final static String client_secret = "OkWylyN3SDpI1cTEC6flAJ8KOc1Ox4gE";
    private static ResteasyClient resteasyClient;
    private static String url = "http://sso-srv:1443/auth";
    private static String scope_url = "http://sso:1443/auth/admin/realms/roham-soft/client-scopes";

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
        return new Config(url, realm_admin, null, null, admin_client_id, null);
    }

    public static Config scopeConfig() {
        //add authorization and JWT to header
        String scopeId = UUID.randomUUID().toString();
        return new Config(scope_url + "/" + scopeId, null, null, null, null, null);
    }
}