package com.authguard.AuthGuard.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenerateTokenDto {
    private String accessToken;
    private LocalDateTime accessTokenExpirationTime;
    private String refreshToken;
    private LocalDateTime refreshTokenExpirationTime;
}
