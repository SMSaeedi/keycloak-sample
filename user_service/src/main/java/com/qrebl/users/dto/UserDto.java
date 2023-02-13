package com.qrebl.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
}