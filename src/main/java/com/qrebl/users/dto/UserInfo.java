package com.qrebl.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserInfo {
    private String[] permissions;
    private String snn;
    private String given_name;
    private String family_name;
    private String mobileNr;
    private RealmAccessDto realm_access;
}
