package com.mediaflow.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mediaflow.api.model.ContentType;
import com.mediaflow.api.model.File;

public interface FileRepository extends  JpaRepository<File, Integer>{
    @Query("SELECT f FROM File f WHERE f.user.userId = :userId AND f.contentType = :contentType")
    Page<File> findByUserAndContentType(
            @Param("userId") Integer userId,
            @Param("contentType") ContentType contentType,
            Pageable pageable
    );

    // Traer todos los archivos de un usuario con paginación
    @Query("SELECT f FROM File f WHERE f.user.userId = :userId")
    Page<File> findByUserFiles(
            @Param("userId") Integer userId,
            Pageable pageable
    );
}
