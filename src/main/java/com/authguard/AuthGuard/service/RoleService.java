package com.authguard.AuthGuard.service;

import com.authguard.AuthGuard.exceptions.NotExistingPermissionException;
import com.authguard.AuthGuard.exceptions.NotExistingRoleException;
import com.authguard.AuthGuard.exceptions.NotExistingUserException;
import com.authguard.AuthGuard.models.*;
import com.authguard.AuthGuard.models.command.*;
import com.authguard.AuthGuard.models.dto.*;
import com.authguard.AuthGuard.repository.AuthUserRepository;
import com.authguard.AuthGuard.repository.PermissionRepository;
import com.authguard.AuthGuard.repository.RoleRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.authguard.AuthGuard.models.QPermission.permission;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final AuthUserRepository authUserRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Transactional
    public CreateRoleDto createRole(CreateRoleCommand createRoleCommand) {
        if (roleRepository.existsByName(createRoleCommand.getName())){
            throw new IllegalStateException("Role already exist");
        }

        Role role = roleRepository.save(new Role(
                null,
                createRoleCommand.getName(),
                new HashSet<>(),
                new HashSet<>()
        ));

        return modelMapper.map(role, CreateRoleDto.class);
    }

    @Transactional
    public void deleteRole(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotExistingRoleException("not existing role"));

        role.getAuthUsers().clear();

        roleRepository.deleteByName(roleName);
    }

    @Transactional
    public AssignRoleDto assignRoles(AssignRoleCommand assignRoleCommand){
        AuthUser authUser = authUserRepository
                .findByUsername(assignRoleCommand.getUsername())
                .orElseThrow(() -> new NotExistingUserException("Not existing user"));

        List<Role> roles = roleRepository
                .findAllByName(assignRoleCommand.getRoleNames());

        if (roles.isEmpty()){
            throw new IllegalArgumentException("your roles, are invalid");
        }

        roles.forEach(r -> r.addAuthUser(authUser));

        return modelMapper.map(assignRoleCommand, AssignRoleDto.class);
    }

    @Transactional
    public void unassignRole(UnassignRoleCommand unassignRoleCommand) {
        AuthUser authUser = authUserRepository
                .findByUsername(unassignRoleCommand.getUsername())
                .orElseThrow(() -> new NotExistingUserException("Not existing user"));

        Role role = roleRepository
                .findByNameWithAuthUsers(unassignRoleCommand.getRoleName())
                .orElseThrow(() -> new NotExistingRoleException("Not existing role"));

        role.getAuthUsers().removeIf(a -> a.getUsername().equals(authUser.getUsername()));
    }

    @Transactional
    public AssignPermissionDto assignPermission(AssignPermissionCommand assignPermissionCommand){
        Role role = roleRepository
                .findByName(assignPermissionCommand.getRoleName())
                .orElseThrow(() -> new NotExistingRoleException("not existing role"));

        List<Permission> permissions = permissionRepository
                .findAllByName(assignPermissionCommand.getPermissionNames());

        if (permissions.isEmpty()){
            throw new IllegalArgumentException("your permissions, are invalid");
        }

        permissions.forEach(role::addPermission);

        return modelMapper.map(assignPermissionCommand, AssignPermissionDto.class);
    }

    @Transactional
    public void unassignPermission(UnassignPermissionCommand unassignPermissionCommand){
        Permission permission = permissionRepository
                .findByName(unassignPermissionCommand.getPermissionName())
                .orElseThrow(() -> new NotExistingPermissionException("Not existing permission"));

        Role role = roleRepository
                .findByNameWithPermissions(unassignPermissionCommand.getRoleName())
                .orElseThrow(() -> new NotExistingRoleException("Not existing role"));

        role.getPermissions().removeIf(p -> p.getName().equals(permission.getName()));
    }

    @Transactional(readOnly = true)
    public Page<RoleDto> findAll(String s, Pageable pageable) {
        QRole role = QRole.role;
        BooleanExpression predicate = buildPredicate(s, role);

        List<RoleDto> roles = new JPAQuery<>(entityManager)
                .select(role)
                .from(role)
                .leftJoin(role.permissions, permission).fetchJoin()
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(r -> new RoleDto(r.getName(), r.getPermissions()))
                .toList();


        long total = new JPAQuery<>(entityManager)
                .select(role)
                .from(role)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(roles, pageable, total);
    }

    private BooleanExpression buildPredicate(String s, QRole role) {
        BooleanExpression predicate = role.isNotNull();


        if(!Objects.isNull(s) && !s.isEmpty()){
            BooleanExpression namePredicate = role.name.equalsIgnoreCase(s);
            BooleanExpression permissionPredicate = role.permissions.any().name.equalsIgnoreCase(s);
            predicate = predicate.and(namePredicate.or(permissionPredicate));
        }

        return predicate;
    }

    @Transactional(readOnly = true)
    public List<String> findRolesNotHavingAuthUser(String username){
        AuthUser authUser = authUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotExistingUserException("Not existing user"));

        return roleRepository.findRolesNotHavingAuthUser(authUser.getId())
                .stream().map(Role::getName)
                .toList();
    }
}
