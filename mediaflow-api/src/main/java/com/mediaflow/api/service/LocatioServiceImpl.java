package com.mediaflow.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mediaflow.api.dto.LocationResponse;
import com.mediaflow.api.dto.LocationRquest;
import com.mediaflow.api.mapper.LocationMapper;
import com.mediaflow.api.model.Location;
import com.mediaflow.api.repository.LocationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocatioServiceImpl implements LocationService{
    private final LocationRepository repository;

    @Override
    public List<LocationResponse> findAll() {
        return repository.findAll().stream()
                .map(LocationMapper::toResponse)
                .toList();
    }

    @Override
    public LocationResponse findById(Integer locationId) {
        Location location = repository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found: " + locationId));
        return LocationMapper.toResponse(location);
    }

    @Override
    public LocationResponse create(LocationRquest req) {
        Location saved = repository.save(LocationMapper.toEntity(req));
        return LocationMapper.toResponse(saved);
    }

    @Override
    public LocationResponse update(Integer locationId, LocationRquest req) {
        Location existing = repository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found: " + locationId));
        LocationMapper.copyToEntity(req, existing);
        Location saved = repository.save(existing);
        return LocationMapper.toResponse(saved);
    }

    @Override
    public void delete(Integer locationId) {
        if (!repository.existsById(locationId)) {
            throw new EntityNotFoundException("Location not found: " + locationId);
        }
        repository.deleteById(locationId);
    }
    
}
