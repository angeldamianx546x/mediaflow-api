package com.mediaflow.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locatioId;

    @Column(name = "country", nullable = false, length = 70)
    private String country;

    @Column(name = "region", nullable = false, length = 70)
    private String region;

    @Column(name = "city", nullable = false, length = 70)
    private String city;

    @Column(name = "lat", nullable = false)
    private float lat;

    @Column(name = "lng", nullable = false)
    private float lng; 
}
