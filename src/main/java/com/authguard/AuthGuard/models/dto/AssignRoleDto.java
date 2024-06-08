package com.authguard.AuthGuard.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssignRoleDto {
    private String username;
    private List<String> roleName;
}
