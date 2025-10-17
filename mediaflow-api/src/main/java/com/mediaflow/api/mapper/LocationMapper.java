package com.mediaflow.api.mapper;

import com.mediaflow.api.dto.LocationResponse;
import com.mediaflow.api.dto.LocationRquest;
import com.mediaflow.api.model.Location;

public class LocationMapper {
    public static LocationResponse toResponse(Location location) {
        if (location == null)
            return null;
        return LocationResponse.builder()
                .locationId(location.getLocatioId())
                .country(location.getCountry())
                .region(location.getRegion())
                .city(location.getCity())
                .lat(location.getLat())
                .lng(location.getLng())
                .build();
    }

    public static Location toEntity(LocationRquest dto) {
        if (dto == null)
            return null;
        return Location.builder()
                .country(dto.getCountry())
                .region(dto.getRegion())
                .city(dto.getCity())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .build();
    }

    public static void copyToEntity(LocationRquest dto, Location entity) {
        if (dto == null || entity == null)
            return;
        entity.setCountry(dto.getCountry());
        entity.setRegion(dto.getRegion());
        entity.setCity(dto.getCity());
        entity.setLat(dto.getLat());
        entity.setLng(dto.getLng());
    }
}
