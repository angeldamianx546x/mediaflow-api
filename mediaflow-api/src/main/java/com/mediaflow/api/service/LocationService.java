package com.mediaflow.api.service;

import java.util.List;

import com.mediaflow.api.dto.LocationResponse;
import com.mediaflow.api.dto.LocationRquest;

public interface LocationService {
    List<LocationResponse> findAll();

    LocationResponse findById(Integer locationId);

    LocationResponse create(LocationRquest req);

    LocationResponse update(Integer locationId, LocationRquest req);

    void delete(Integer locationId);
}
