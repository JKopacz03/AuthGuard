package com.authguard.AuthGuard.service;

import com.authguard.AuthGuard.exceptions.IncorrectPasswordException;
import com.authguard.AuthGuard.exceptions.NotExistingUserException;
import com.authguard.AuthGuard.models.AuthUser;
import com.authguard.AuthGuard.models.QAuthUser;
import com.authguard.AuthGuard.models.command.ModifyPasswordAuthUserCommand;
import com.authguard.AuthGuard.models.dto.AuthUserDto;
import com.authguard.AuthGuard.models.dto.FindUsersDto;
import com.authguard.AuthGuard.repository.AuthUserRepository;
import com.authguard.AuthGuard.repository.RoleRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.authguard.AuthGuard.models.QRole.role;

@Service
@RequiredArgsConstructor
public class AuthUserService {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Transactional
    public void deleteUser(String username) {
        AuthUser user = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new NotExistingUserException("not existing user"));

        user.getRoles()
             .forEach(r -> r.getAuthUsers().remove(user));

        authUserRepository.deleteByUsername(username);
    }

    @Transactional
    public void modifyPassword(ModifyPasswordAuthUserCommand command) {
        if (command.getCurrentPassword().equalsIgnoreCase(command.getNewPassword())){
           throw new IllegalArgumentException("New password cannot be same like current");
        }

        AuthUser authUser = authUserRepository
                .findByUsername(command.getUsername())
                .orElseThrow(() -> new NotExistingUserException("Not existing user"));

        if (!passwordEncoder.matches(command.getCurrentPassword(), authUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        authUser.setPassword(passwordEncoder.encode(command.getNewPassword()));
    }

    @Transactional(readOnly = true)
    public Page<AuthUserDto> findAll(String s, Pageable pageable) {
        QAuthUser authUser = QAuthUser.authUser;
        BooleanExpression predicate = buildPredicate(s, authUser);

        List<AuthUserDto> users = new JPAQuery<>(entityManager)
                .select(authUser)
                .from(authUser)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(r -> new AuthUserDto(r.getUsername(), r.getRoles()))
                .toList();

        long total = new JPAQuery<>(entityManager)
                .select(authUser)
                .from(authUser)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(users, pageable, total);
    }

    private BooleanExpression buildPredicate(String s, QAuthUser authUser) {
        BooleanExpression predicate = authUser.isNotNull();

        if (!Objects.isNull(s) && !s.isEmpty()) {
            BooleanExpression usernamePredicate = authUser.username.equalsIgnoreCase(s);
            BooleanExpression roleNamePredicate = authUser.roles.any().name.equalsIgnoreCase(s);
            predicate = predicate.and(usernamePredicate.or(roleNamePredicate));
        }

        return predicate;
    }


    public Long getUsersAmount() {
        return authUserRepository.count();
    }
}
