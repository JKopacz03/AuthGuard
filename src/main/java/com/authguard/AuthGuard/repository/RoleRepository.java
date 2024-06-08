package com.authguard.AuthGuard.repository;

import com.authguard.AuthGuard.models.Permission;
import com.authguard.AuthGuard.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Transactional
    void deleteByName(String roleName);

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.authUsers WHERE r.name = :roleName")
    Optional<Role> findByNameWithAuthUsers(@Param("roleName") String roleName);

    @Query("SELECT r FROM Role r WHERE r.id NOT IN (SELECT r2.id FROM Role r2 JOIN r2.authUsers au WHERE au.id = :authUserId)")
    List<Role> findRolesNotHavingAuthUser(@Param("authUserId") Long authUserId);

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.name = :roleName")
    Optional<Role> findByNameWithPermissions(@Param("roleName") String roleName);

    boolean existsByName(String name);

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.authUsers WHERE r.name IN :roleNames")
    List<Role> findAllByName(@Param("roleNames") List<String> roleNames);

    @Query("SELECT r FROM Role r JOIN FETCH r.permissions p WHERE p.name = :permissionName")
    List<Role> findRolesHavingPermission(@Param("permissionName") String permissionName);
}
