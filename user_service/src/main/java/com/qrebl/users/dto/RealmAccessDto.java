package com.qrebl.users.dto;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RealmAccessDto {
    private Set<String> roles;
}
