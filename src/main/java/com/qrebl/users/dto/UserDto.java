package com.qrebl.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UserDto {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
}