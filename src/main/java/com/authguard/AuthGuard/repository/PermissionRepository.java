package com.authguard.AuthGuard.repository;

import com.authguard.AuthGuard.models.Permission;
import com.authguard.AuthGuard.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String permissionName);

    @Transactional
    void deleteByName(String permissionName);

    boolean existsByName(String name);

    @Query("SELECT p FROM Permission p WHERE p NOT IN (SELECT r.permissions FROM Role r WHERE r.id = :roleId)")
    List<Permission> findPermissionsNotHavingRole(@Param("roleId") Long roleId);

    @Query("SELECT p FROM Permission p WHERE p.name IN :permissionNames")
    List<Permission> findAllByName(@Param("permissionNames") List<String> permissionNames);
}
