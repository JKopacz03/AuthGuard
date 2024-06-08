package com.authguard.AuthGuard.service;

import com.authguard.AuthGuard.exceptions.IncorrectPasswordException;
import com.authguard.AuthGuard.exceptions.JwtTokenException;
import com.authguard.AuthGuard.exceptions.NotExistingUserException;
import com.authguard.AuthGuard.models.AuthUser;
import com.authguard.AuthGuard.models.command.AddUserCommand;
import com.authguard.AuthGuard.models.command.GenerateTokenCommand;
import com.authguard.AuthGuard.models.command.RefreshTokenCommand;
import com.authguard.AuthGuard.models.dto.GenerateTokenDto;
import com.authguard.AuthGuard.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public GenerateTokenDto generateJwtToken(GenerateTokenCommand command){
        AuthUser authUser = authUserRepository
                .findByUsername(command.getUsername())
                .orElseThrow(() -> new NotExistingUserException("Not existing user"));

        if (!passwordEncoder.matches(command.getPassword(), authUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        return new GenerateTokenDto(
                jwtService.generateToken(authUser),
                LocalDateTime.now().plusMinutes(30),
                jwtService.generateRefreshToken(authUser.getUsername()),
                LocalDateTime.now().plusMonths(6));
    }

    @Transactional
    public GenerateTokenDto validateRefreshToken(RefreshTokenCommand refreshToken) {
        String username = jwtService.extractUsername(refreshToken.getRefreshToken());
        AuthUser authUser = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new NotExistingUserException("Not existing user"));

        if (jwtService.isRefreshTokenValid(refreshToken.getRefreshToken(), authUser)) {
            String accessToken = jwtService.generateToken(authUser);
            return new GenerateTokenDto(accessToken,
                    LocalDateTime.now().plusMinutes(30),
                    refreshToken.getRefreshToken(),
                    LocalDateTime.now().plusMonths(6));
        } else {
            throw new JwtTokenException("Invalid refresh token");
        }
    }

    @Transactional
    public GenerateTokenDto addUser(AddUserCommand addUserCommand) {
        if (authUserRepository.existsByUsername(addUserCommand.getUsername())){
            throw new IllegalArgumentException("Username already exist");
        }

        AuthUser authUser = new AuthUser(
                null,
                addUserCommand.getUsername(),
                passwordEncoder.encode(addUserCommand.getPassword()),
                new HashSet<>(),
                1
        );

        return new GenerateTokenDto(
                jwtService.generateToken(authUserRepository.save(authUser)),
                LocalDateTime.now().plusMinutes(30),
                jwtService.generateRefreshToken(authUser.getUsername()),
                LocalDateTime.now().plusMonths(6));
    }
}
