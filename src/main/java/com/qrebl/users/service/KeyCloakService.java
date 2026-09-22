package com.qrebl.users.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrebl.users.config.Credentials;
import com.qrebl.users.config.KeycloakConfig;
import com.qrebl.users.dto.AuthDto;
import com.qrebl.users.dto.JwtDto;
import com.qrebl.users.dto.LoginDto;
import com.qrebl.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeyCloakService {
    private final ObjectMapper objectMapper;
    private final KeycloakConfig keycloakConfig;

    public void addUser(UserDto userDto) {
        CredentialRepresentation credential = Credentials.createPasswordCredentials(userDto.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setAttributes(Map.of(
                "ssn", List.of("123456789"),
                "mobileNr", List.of("123456789")));
        user.setUsername(userDto.getUserName());
        user.setFirstName(userDto.getFirstname());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmailId());
        user.setEnabled(true);
        user.setCredentials(List.of(credential));

        getInstance().create(user);
    }

    public List<UserRepresentation> getUser(String userName) {
        return getInstance().search(userName, true);
    }

    public void updateUser(String userId, UserDto userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUserName());
        user.setFirstName(userDto.getFirstname());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmailId());
        user.setCredentials(List.of(Credentials.createPasswordCredentials(userDto.getPassword())));

        getInstance().get(userId).update(user);
    }

    public void deleteUser(String userId) {
        getInstance().get(userId).remove();
    }

    public AuthDto getToken(LoginDto loginDto) {
        Config config = keycloakConfig.newConfig(
                loginDto.getRealm(),
                loginDto.getUsername(),
                loginDto.getPassword(),
                loginDto.getClientId(),
                loginDto.getClientSecret());
        TokenManager tokenManager = new TokenManager(config, keycloakConfig.getResteasyClient());
        return convertToAuthDto(tokenManager);
    }

    private AuthDto convertToAuthDto(TokenManager tokenManager) {
        String accessToken = tokenManager.getAccessTokenString();
        DecodedJWT jwt = JWT.decode(accessToken);
        try {
            String payload = new String(
                    Base64.getUrlDecoder().decode(jwt.getPayload()),
                    StandardCharsets.UTF_8);
            JwtDto jwtDto = objectMapper.readValue(payload, JwtDto.class);
            Date expiresAt = jwt.getExpiresAt();
            return AuthDto.builder()
                    .accessToken(accessToken)
                    .expireTime(expiresAt == null ? null : expiresAt.toString())
                    .firstName(jwtDto.getFirstName())
                    .lastName(jwtDto.getLastName())
                    .scopes(jwtDto.getScopes())
                    .build();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to decode the Keycloak access token", exception);
        }
    }

    public void sendVerificationLink(String userId) {
        getInstance().get(userId).sendVerifyEmail();
    }

    public void sendResetPassword(String userId) {
        getInstance().get(userId).executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

    private UsersResource getInstance() {
        return keycloakConfig.getInstance().realm(keycloakConfig.getConfiguredRealm()).users();
    }
}
