package com.mediaflow.api.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mediaflow.api.dto.LocationResponse;
import com.mediaflow.api.dto.LoguinReques;
import com.mediaflow.api.dto.UserAuth;
import com.mediaflow.api.dto.UserRequest;
import com.mediaflow.api.dto.UserResponse;
import com.mediaflow.api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Tag(name = "Providers", description = "Provides methods for managing providers")
public class UserController {

    private final UserService service;

    @Operation(summary = "Register new user", description = "Creates a new user in the system with profile and location")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    @PostMapping("register")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req) {
        UserResponse created = service.create(req);
        return ResponseEntity
                .created(URI.create("/api/v1/users/" + created.getUserId()))
                .body(created);
    }
    
    @Operation(summary = "Update user account", description = "Updates an existing user's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/update_account/{userId}")
    public UserResponse update(@PathVariable Integer userId, @Valid @RequestBody UserRequest req) {
        return service.update(userId, req);
    }

    @Operation(summary = "Delete user account", description = "Permanently deletes a user from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/delete_account/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer userId) {
        service.delete(userId);
    }

    @Operation(summary = "User login", description = "Authenticates a user with email and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UserAuth.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    public ResponseEntity<UserAuth> login(@RequestBody LoguinReques reques) {
        return ResponseEntity.ok(service.login(reques.getEmail(), reques.getPassword()));
    }

    @Operation(summary = "Get user location", description = "Returns the location information associated with a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location retrieved successfully", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = LocationResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found or has no assigned location")
    })
    @GetMapping("/{id}/location")
    public ResponseEntity<LocationResponse> getUserLocation(@PathVariable Integer id) {
        LocationResponse location = service.getUserLocation(id);
        return ResponseEntity.ok(location);
    }
}
