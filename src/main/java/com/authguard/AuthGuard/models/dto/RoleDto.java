package com.authguard.AuthGuard.models.dto;

import com.authguard.AuthGuard.models.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class RoleDto {
    private String name;
    private Set<String> permissions;

    public RoleDto(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}
