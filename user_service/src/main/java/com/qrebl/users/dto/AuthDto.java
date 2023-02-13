package com.qrebl.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthDto {
    private String accessToken;
    @JsonProperty("exp")
    private String expireTime;
    @JsonProperty("given_name")
    private String firstName;
    @JsonProperty("family_name")
    private String lastName;
    private String scopes;
}
