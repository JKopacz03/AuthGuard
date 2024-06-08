package com.authguard.AuthGuard.models.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignPermissionCommand {
    @NotBlank(message = "roleName is mandatory")
    private String roleName;
    private List<String> permissionNames;
}
