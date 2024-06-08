package com.authguard.AuthGuard.controller;

import com.authguard.AuthGuard.models.command.GenerateTokenCommand;
import com.authguard.AuthGuard.models.dto.GenerateAdminTokenDto;
import com.authguard.AuthGuard.service.AuthAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin
public class AdminAuthController {
    private final AuthAdminService authUserService;

    @PostMapping("/generate-token")
    public ResponseEntity<GenerateAdminTokenDto> generateAdminJwtToken(@Valid @RequestBody GenerateTokenCommand generateTokenCommand){
        return new ResponseEntity<>(authUserService.generateAdminJwtToken(generateTokenCommand), HttpStatus.OK);
    }
}
