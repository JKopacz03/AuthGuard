package com.authguard.AuthGuard.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class GenerateAdminTokenDto {
    private String accessToken;
    private LocalDateTime accessTokenExpirationTime;
}
