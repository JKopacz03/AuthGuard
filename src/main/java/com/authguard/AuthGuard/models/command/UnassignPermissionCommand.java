package com.authguard.AuthGuard.models.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnassignPermissionCommand {
    @NotBlank(message = "permissionName is mandatory")
    private String permissionName;
    @NotBlank(message = "roleName is mandatory")
    private String roleName;
}
