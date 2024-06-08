package com.authguard.AuthGuard.controller;

import com.authguard.AuthGuard.models.AuthUser;
import com.authguard.AuthGuard.models.command.ModifyPasswordAuthUserCommand;
import com.authguard.AuthGuard.models.dto.AuthUserDto;
import com.authguard.AuthGuard.models.dto.RoleDto;
import com.authguard.AuthGuard.service.AuthUserService;
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
@RequestMapping("/user")
@CrossOrigin
public class AuthUserController {
    private final AuthUserService authUserService;

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable("username") String username){
        authUserService.deleteUser(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity<Page<AuthUserDto>> findUsers(@RequestParam(required = false) String s,
                                                    @PageableDefault Pageable pageable){
        return new ResponseEntity<>(authUserService.findAll(s, pageable), HttpStatus.OK);
    }

    @GetMapping("/users-amount")
    public ResponseEntity<Long> usersAmount(){
        return new ResponseEntity<>(authUserService.getUsersAmount(), HttpStatus.OK);
    }
}
