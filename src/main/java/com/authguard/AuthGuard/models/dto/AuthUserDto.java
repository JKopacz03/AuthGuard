package com.authguard.AuthGuard.models.dto;

import com.authguard.AuthGuard.models.Role;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class AuthUserDto {
    private String username;
    private Set<String> roles;

    public AuthUserDto(String username, Set<Role> roles) {
        this.username = username;
        this.roles = roles.stream().map(Role::getName)
                .collect(Collectors.toSet());
    }
}
