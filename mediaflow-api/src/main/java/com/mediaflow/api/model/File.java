package com.mediaflow.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "width", nullable=false)
    private Integer width;

    @Column(name = "height", nullable=false)
    private Integer height;

    @Column(name = "format", nullable=false,length=6)
    private String format;

    @Column(name = "file_size_mb",nullable=false)
    private Integer fileSizeMB;

    @Column(name = "duration_seconds", nullable=false)
    private Integer durationSeconds;

    @Column(name = "language", nullable=false, length=20)
    private String language;

    @Column(name = "title" ,nullable=false, length=100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "recommended_age")
    private Integer recommendedAge;

    @Column(name = "storage_url",nullable=false, columnDefinition = "TEXT")
    private String storageUrl;

    @Column(name = "thumbnail_url",nullable=false, columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(name = "created", nullable=false)
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

