package com.authguard.AuthGuard.repository;

import com.authguard.AuthGuard.models.AuthAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthAdminRepository extends JpaRepository<AuthAdmin, Long> {
    Optional<AuthAdmin> findByUsername(String username);

}
