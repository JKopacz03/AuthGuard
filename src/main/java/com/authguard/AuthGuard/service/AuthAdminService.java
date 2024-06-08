package com.authguard.AuthGuard.service;

import com.authguard.AuthGuard.exceptions.NotExistingUserException;
import com.authguard.AuthGuard.models.AuthAdmin;
import com.authguard.AuthGuard.models.command.GenerateTokenCommand;
import com.authguard.AuthGuard.models.dto.GenerateAdminTokenDto;
import com.authguard.AuthGuard.repository.AuthAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthAdminService {
    private final AuthAdminRepository authAdminRepository;
    private final AdminJwtService adminJwtService;
    private final AuthenticationManager authenticationManager;

    public GenerateAdminTokenDto generateAdminJwtToken(GenerateTokenCommand command) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.getUsername(),
                        command.getPassword()
                )
        );

        AuthAdmin authAdmin = authAdminRepository
                .findByUsername(command.getUsername())
                .orElseThrow(() -> new NotExistingUserException("Not existing admin account"));

        return new GenerateAdminTokenDto(
                adminJwtService.generateToken(authAdmin),
                LocalDateTime.now().plusDays(1));
    }
}
