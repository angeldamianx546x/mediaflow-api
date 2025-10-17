package com.mediaflow.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mediaflow.api.dto.ProfileRequest;
import com.mediaflow.api.dto.ProfileResponse;
import com.mediaflow.api.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class ProfileController {
    private final ProfileService service;

    @PutMapping("/{profileId}")
    public ProfileResponse update(@PathVariable Integer profileId, @Valid @RequestBody ProfileRequest req) {
        return service.update(profileId, req);
    }

    @DeleteMapping("/{profileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer profileId) {
        service.delete(profileId);
    }
}
