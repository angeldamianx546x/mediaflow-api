package com.mediaflow.api.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;

import com.mediaflow.api.dto.AuthResponse;
import com.mediaflow.api.dto.LocationResponse;
import com.mediaflow.api.dto.UserAuth;
import com.mediaflow.api.dto.UserRequest;
import com.mediaflow.api.dto.UserResponse;
import com.mediaflow.api.graphql.input.LoginInput;
import com.mediaflow.api.graphql.input.UserRegistrationInput;
import com.mediaflow.api.graphql.input.UserUpdateInput;
import com.mediaflow.api.service.AuthenticationService;
import com.mediaflow.api.service.JwtService;
import com.mediaflow.api.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserGraphQLController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final AuthenticationService authenticationService;

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public UserResponse me() {
        String email = authenticationService.getCurrentUserEmail();
        return userService.findByEmail(email);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public UserResponse user(@Argument Integer userId) {
        if (!authenticationService.canAccess(userId)) {
            throw new SecurityException("No tienes permiso para ver esta información");
        }
        return userService.findById(userId);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public LocationResponse userLocation(@Argument Integer userId) {
        if (!authenticationService.canAccess(userId)) {
            throw new SecurityException("No tienes permiso para ver esta ubicación");
        }
        return userService.getUserLocation(userId);
    }

    @MutationMapping
    public AuthResponse login(@Argument LoginInput input) {
        // Autenticar usuario
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
            )
        );

        // Obtener datos del usuario
        UserAuth user = userService.login(input.getEmail(), input.getPassword());
        
        // Generar token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(input.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        // Construir respuesta con token
        return AuthResponse.builder()
                .token(jwtToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .userId(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .dateBirth(user.getDateBirth())
                .roles(user.getRoles())
                .profileId(user.getProfileId())
                .displayName(user.getDisplayName())
                .preferredLanguage(user.getPreferredLanguage())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .build();
    }

    @MutationMapping
    public UserResponse register(@Argument UserRegistrationInput input) {
        UserRequest req = input.toUserRequest();
        return userService.create(req);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public UserResponse updateUser(@Argument Integer userId, @Argument UserUpdateInput input) {
        if (!authenticationService.canAccess(userId)) {
            throw new SecurityException("No tienes permiso para actualizar esta cuenta");
        }
        UserRequest req = input.toUserRequest();
        return userService.update(userId, req);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Boolean deleteUser(@Argument Integer userId) {
        if (!authenticationService.canAccess(userId)) {
            throw new SecurityException("No tienes permiso para eliminar esta cuenta");
        }
        userService.delete(userId);
        return true;
    }
}
