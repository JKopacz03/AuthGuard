package com.authguard.AuthGuard.models.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPasswordAuthUserCommand {
    @NotBlank(message = "username is mandatory")
    private String username;
    @NotBlank(message = "currentPassword is mandatory")
    private String currentPassword;
    @NotBlank(message = "newPassword is mandatory")
    private String newPassword;
}
