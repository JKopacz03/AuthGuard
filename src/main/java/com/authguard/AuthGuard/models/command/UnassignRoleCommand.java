package com.authguard.AuthGuard.models.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnassignRoleCommand {
    @NotBlank(message = "username is mandatory")
    private String username;
    @NotBlank(message = "roleName is mandatory")
    private String roleName;
}
