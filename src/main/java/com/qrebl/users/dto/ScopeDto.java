package com.qrebl.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScopeDto {
    private String scopeId;
    private String name;
    private String description;
    private String protocol;
}