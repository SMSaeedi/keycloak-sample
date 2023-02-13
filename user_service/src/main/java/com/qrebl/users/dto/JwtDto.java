package com.qrebl.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto extends AuthDto{
    @JsonProperty("iss")
    private String realmUrl;
    private String iat;
    private String aud;
    private String sub;
    private String jti;
    private String session_state;
    private String acr;
    private String sid;
    private String email_verified;
    private String name;
    @JsonProperty("preferred_username")
    private String userName;
    @JsonProperty("azp")
    private String clientId;
    @JsonProperty("typ")
    private String authenticationType;
}