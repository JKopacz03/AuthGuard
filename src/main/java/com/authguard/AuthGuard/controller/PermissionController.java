package com.authguard.AuthGuard.controller;

import com.authguard.AuthGuard.models.command.AddPermissionCommand;
import com.authguard.AuthGuard.models.dto.AddPermissionDto;
import com.authguard.AuthGuard.models.dto.PermissionDto;
import com.authguard.AuthGuard.service.PermissionService;
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
@RequestMapping("/permission")
@CrossOrigin
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/add-permission")
    public ResponseEntity<AddPermissionDto> addPermission(@Valid @RequestBody AddPermissionCommand addPermissionCommand){
        return new ResponseEntity<>(permissionService.addPermission(addPermissionCommand), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{permissionName}")
    public ResponseEntity<Void> deletePermission(@PathVariable("permissionName") String permissionName) {
        permissionService.deletePermission(permissionName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity<Page<PermissionDto>> findPermissions(@RequestParam(required = false) String s, @PageableDefault Pageable pageable){
        return new ResponseEntity<>(permissionService.findAll(s, pageable), HttpStatus.OK);
    }

    @GetMapping("/find-beside-permissions/{roleName}")
    public ResponseEntity<List<String>> findPermissionsNotHavingRole(@PathVariable("roleName") String roleName){
        return new ResponseEntity<>(permissionService.findPermissionsNotHavingRole(roleName), HttpStatus.OK);
    }
}
