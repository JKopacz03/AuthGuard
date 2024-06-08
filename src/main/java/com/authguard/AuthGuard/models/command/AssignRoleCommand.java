package com.authguard.AuthGuard.models.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignRoleCommand {
    @NotBlank(message = "username is mandatory")
    private String username;
    private List<String> roleNames;
}
