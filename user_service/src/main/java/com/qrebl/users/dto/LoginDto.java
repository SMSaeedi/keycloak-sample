package com.qrebl.users.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String realm;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String grantType;
}
