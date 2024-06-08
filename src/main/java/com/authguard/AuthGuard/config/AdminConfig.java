package com.authguard.AuthGuard.config;

import com.authguard.AuthGuard.models.AuthAdmin;
import com.authguard.AuthGuard.models.AuthUser;
import com.authguard.AuthGuard.repository.AuthAdminRepository;
import com.authguard.AuthGuard.repository.AuthUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
public class AdminConfig {
    private final AuthAdminRepository authAdminRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${authguard.username}")
    private String username;
    @Value("${authguard.password}")
    private String password;

    @PostConstruct
    public void addAdmin(){
        authAdminRepository.findByUsername(username)
                .orElseGet(() -> authAdminRepository.save(
                        new AuthAdmin(null, username, passwordEncoder.encode(password))
                ));
    }
}
