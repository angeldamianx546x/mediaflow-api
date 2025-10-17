package com.mediaflow.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mediaflow.api.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("""
        SELECT l FROM Location l
        WHERE l.country = :country 
          AND l.region = :region 
          AND l.city = :city 
          AND l.lat = :lat 
          AND l.lng = :lng
    """)
    Optional<Location> findExisting(@Param("country") String country,
            @Param("region") String region,
            @Param("city") String city,
            @Param("lat") float lat,
            @Param("lng") float lng);
}
