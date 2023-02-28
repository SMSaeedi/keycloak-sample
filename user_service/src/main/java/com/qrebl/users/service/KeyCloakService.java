package com.qrebl.users.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrebl.users.dto.*;
import com.qrebl.users.config.Credentials;
import com.qrebl.users.config.KeycloakConfig;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@AllArgsConstructor
@Service
@RequiredArgsConstructor
public class KeyCloakService {
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        modelMapper = new ModelMapper();
        objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
    }

    public void addUser(UserDto userDTO) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();

        Map<String, List<String>> attributes = new HashMap<>();

        attributes.put("ssn", Collections.singletonList("123456789"));
        attributes.put("mobileNr", Collections.singletonList("123456789"));

        user.setAttributes(attributes);
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        UsersResource instance = getInstance();
        instance.create(user);
    }

    public List<UserRepresentation> getUser(String userName) {
        UsersResource usersResource = getInstance();
        List<UserRepresentation> user = usersResource.search(userName, true);
        return user;
    }

    public void updateUser(String userId, UserDto userDTO) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = getInstance();
        usersResource.get(userId).update(user);
    }

    public void deleteUser(String userId) {
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .remove();
    }

    public AuthDto getToken(LoginDto loginDto) {
        Config config = KeycloakConfig.newConfig();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        modelMapper.map(loginDto, config);

        TokenManager tokenManager = new TokenManager(config, KeycloakConfig.getResteasyClient());

        return convertToAuthDto(tokenManager);
    }

    @SneakyThrows
    private AuthDto convertToAuthDto(TokenManager tokenManager) {
        DecodedJWT jwt = JWT.decode(tokenManager.getAccessTokenString());
        byte[] decodedBytes = Base64.getDecoder().decode(jwt.getPayload());
        String decodedString = new String(decodedBytes);
        JwtDto jwtDto = objectMapper.readValue(decodedString, JwtDto.class);
        jwtDto.setAccessToken(jwt.getPayload());

        return AuthDto.builder()
                .accessToken(jwtDto.getAccessToken())
                .expireTime(String.valueOf(new Date(Long.parseLong(jwtDto.getExpireTime()))))
                .firstName(jwtDto.getFirstName())
                .lastName(jwtDto.getLastName())
                .scopes(jwtDto.getScopes())
                .build();
    }

    public void sendVerificationLink(String userId) {
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .sendVerifyEmail();
    }

    public void sendResetPassword(String userId) {
        UsersResource usersResource = getInstance();

        usersResource.get(userId)
                .executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }

    public UsersResource getInstance() {
        return KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
    }

    public ScopeDto addScope(ScopeDto scopeDto) {
        Config config = KeycloakConfig.scopeConfig();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        modelMapper.map(scopeDto, config);

        return ScopeDto.builder()
//                .scopeId(UUID.randomUUID().toString())
                .name(scopeDto.getName())
                .protocol(scopeDto.getProtocol())
                .description(scopeDto.getDescription())
                .build();
    }
}