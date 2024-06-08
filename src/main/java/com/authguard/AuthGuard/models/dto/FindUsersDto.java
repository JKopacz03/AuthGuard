package com.authguard.AuthGuard.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindUsersDto {
    private Long score;
    private Set<AuthUserDto> users;

}
