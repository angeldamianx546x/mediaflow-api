package com.mediaflow.api.controller;

import java.net.URI;
import java.util.List;

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

import com.mediaflow.api.dto.RoleRequest;
import com.mediaflow.api.dto.RoleResponse;
import com.mediaflow.api.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class RoleController {
    private final RoleService service;

    @GetMapping
    public List<RoleResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{roleId}")
    public RoleResponse getById(@PathVariable Integer roleId) {
        return service.findById(roleId);
    }

    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest req) {
        RoleResponse created = service.create(req);
        return ResponseEntity
                .created(URI.create("/api/roles/" + created.getRoleId()))
                .body(created);
    }
    
    @PutMapping("/{roleId}")
    public RoleResponse update(@PathVariable Integer roleId, @Valid @RequestBody RoleRequest req) {
        return service.update(roleId, req);
    }

    @DeleteMapping("/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer roleId) {
        service.delete(roleId);
    }
    
}
