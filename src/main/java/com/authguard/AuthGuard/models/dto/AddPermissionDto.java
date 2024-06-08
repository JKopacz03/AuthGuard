package com.authguard.AuthGuard.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPermissionDto {
    private Long id;
    private String name;
}

