package com.authguard.AuthGuard.controller;

import com.authguard.AuthGuard.models.command.*;
import com.authguard.AuthGuard.models.dto.*;
import com.authguard.AuthGuard.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@CrossOrigin
public class RoleController {
    private final RoleService roleService;
    @PostMapping("/create-role")
    public ResponseEntity<CreateRoleDto> createRole(@Valid @RequestBody CreateRoleCommand createRoleCommand){
        return new ResponseEntity<>(roleService.createRole(createRoleCommand), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{roleName}")
    public ResponseEntity<Void> deleteRole(@PathVariable("roleName") String roleName){
        roleService.deleteRole(roleName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign-roles")
    public ResponseEntity<AssignRoleDto> assignRoles(@Valid @RequestBody AssignRoleCommand assignRoleCommand){
        return new ResponseEntity<>(roleService.assignRoles(assignRoleCommand), HttpStatus.OK);
    }

    @DeleteMapping("/unassign-role")
    public ResponseEntity<Void> unassignRole(@Valid UnassignRoleCommand unassignRoleCommand){
        roleService.unassignRole(unassignRoleCommand);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign-permissions")
    public ResponseEntity<AssignPermissionDto> assignPermission(@Valid @RequestBody AssignPermissionCommand assignPermissionCommand){
        return new ResponseEntity<>(roleService.assignPermission(assignPermissionCommand), HttpStatus.OK);
    }

    @DeleteMapping("/unassign-permission")
    public ResponseEntity<Void> unassignPermission(@Valid UnassignPermissionCommand unassignPermissionCommand){
        roleService.unassignPermission(unassignPermissionCommand);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity<Page<RoleDto>> findRoles(@RequestParam(required = false) String s,
                                                   @PageableDefault Pageable pageable){
        return new ResponseEntity<>(roleService.findAll(s, pageable), HttpStatus.OK);
    }

    @GetMapping("/find-beside-roles/{username}")
    public ResponseEntity<List<String>> findRolesNotHavingAuthUser(@PathVariable("username") String username){
        return new ResponseEntity<>(roleService.findRolesNotHavingAuthUser(username), HttpStatus.OK);
    }
}
