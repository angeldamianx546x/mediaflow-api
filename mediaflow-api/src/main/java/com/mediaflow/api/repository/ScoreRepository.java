package com.mediaflow.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mediaflow.api.model.Score;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    
    @Query("SELECT s FROM Score s WHERE s.content.contentId = :contentId")
    Optional<Score> findByContentId(@Param("contentId") Integer contentId);
}