package com.mediaflow.api.service;

import com.mediaflow.api.dto.LocationResponse;
import com.mediaflow.api.dto.UserAuth;
import com.mediaflow.api.dto.UserRequest;
import com.mediaflow.api.dto.UserResponse;

public interface UserService {

    UserResponse findById(Integer userId);

    UserResponse create(UserRequest req);

    UserResponse update(Integer userId, UserRequest req);

    void delete(Integer userId);

    UserAuth login(String email,String password);
    
    LocationResponse getUserLocation(Integer userId);
}
