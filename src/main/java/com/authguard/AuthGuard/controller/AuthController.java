package com.authguard.AuthGuard.controller;

import com.authguard.AuthGuard.models.command.AddUserCommand;
import com.authguard.AuthGuard.models.command.GenerateTokenCommand;
import com.authguard.AuthGuard.models.command.ModifyPasswordAuthUserCommand;
import com.authguard.AuthGuard.models.command.RefreshTokenCommand;
import com.authguard.AuthGuard.models.dto.GenerateTokenDto;
import com.authguard.AuthGuard.service.AuthService;
import com.authguard.AuthGuard.service.AuthUserService;
import com.authguard.AuthGuard.service.JwtService;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.jwk.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final AuthUserService authUserService;
    private final JwtService jwtService;

    @GetMapping("/jwks")
    public Map<String, Object> getJwks() {
        RSAKey rsaKey = new RSAKey.Builder(jwtService.getPublicKey())
                .privateKey(jwtService.getPrivateKey())
                .keyID("1234")
                .algorithm(Algorithm.parse("RS256"))
                .keyUse(KeyUse.SIGNATURE)
                .build();

        return new JWKSet(rsaKey).toJSONObject();
    }

    @PostMapping("/generate-token")
    public ResponseEntity<GenerateTokenDto> generateJwtToken(@Valid @RequestBody GenerateTokenCommand generateTokenCommand){
        return new ResponseEntity<>(authService.generateJwtToken(generateTokenCommand), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<GenerateTokenDto> refreshToken(@Valid @RequestBody RefreshTokenCommand refreshToken) {
        return new ResponseEntity<>(authService.validateRefreshToken(refreshToken), HttpStatus.OK);
    }

    @PostMapping("/add-user")
    public ResponseEntity<GenerateTokenDto> addUser(@Valid @RequestBody AddUserCommand addUserCommand){
        return new ResponseEntity<>(authService.addUser(addUserCommand), HttpStatus.CREATED);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<Void> modifyPassword(@Valid @RequestBody ModifyPasswordAuthUserCommand modifyPasswordAuthUserCommand){
        authUserService.modifyPassword(modifyPasswordAuthUserCommand);
        return ResponseEntity.ok().build();
    }
}
