package com.authguard.AuthGuard.service;

import com.authguard.AuthGuard.exceptions.NotExistingPermissionException;
import com.authguard.AuthGuard.exceptions.NotExistingRoleException;
import com.authguard.AuthGuard.exceptions.NotExistingUserException;
import com.authguard.AuthGuard.models.AuthUser;
import com.authguard.AuthGuard.models.Permission;
import com.authguard.AuthGuard.models.QPermission;
import com.authguard.AuthGuard.models.Role;
import com.authguard.AuthGuard.models.command.AddPermissionCommand;
import com.authguard.AuthGuard.models.dto.AddPermissionDto;
import com.authguard.AuthGuard.models.dto.PermissionDto;
import com.authguard.AuthGuard.models.dto.RoleDto;
import com.authguard.AuthGuard.repository.PermissionRepository;
import com.authguard.AuthGuard.repository.RoleRepository;
import com.querydsl.core.types.Projections;
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

import java.util.List;
import java.util.Objects;

import static com.authguard.AuthGuard.models.QPermission.permission;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Transactional
    public AddPermissionDto addPermission(AddPermissionCommand command){
        if(permissionRepository.existsByName(command.getName())){
            throw new IllegalArgumentException("Permission already exist");
        }

        Permission permission = permissionRepository.save(modelMapper.map(command, Permission.class));
        return modelMapper.map(permission, AddPermissionDto.class);
    }

    @Transactional
    public void deletePermission(String permissionName) {
        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new NotExistingPermissionException("not existing permission"));

        // TODO QUERY DELETE PERMISSION IN ROLE IF ROLE CONTAINS PERMISSION
        roleRepository.findAll()
                .stream().peek(System.out::println)
                .forEach(r -> r.getPermissions().removeIf(p -> p.getName().equals(permissionName)));

        permissionRepository.deleteByName(permissionName);
    }

    @Transactional(readOnly = true)
    public Page<PermissionDto> findAll(String s, Pageable pageable) {
        QPermission permission = QPermission.permission;
        BooleanExpression predicate = buildPredicate(s, permission);

        List<PermissionDto> permissions = new JPAQuery<>(entityManager)
                .select(Projections.constructor(PermissionDto.class,
                        permission.name))
                .from(permission)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = new JPAQuery<>(entityManager)
                .select(permission)
                .from(permission)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(permissions, pageable, total);
    }

    private BooleanExpression buildPredicate(String s, QPermission permission) {
        BooleanExpression predicate = permission.isNotNull();

        if(!Objects.isNull(s) && !s.isEmpty()){
            predicate = predicate.and(permission.name.equalsIgnoreCase(s));
        }

        return predicate;
    }

    @Transactional(readOnly = true)
    public List<String> findPermissionsNotHavingRole(String roleName) {
        Role role = roleRepository
                .findByName(roleName)
                .orElseThrow(() -> new NotExistingRoleException("Not existing role"));

        return permissionRepository.findPermissionsNotHavingRole(role.getId())
                .stream().map(Permission::getName)
                .toList();
    }
}
