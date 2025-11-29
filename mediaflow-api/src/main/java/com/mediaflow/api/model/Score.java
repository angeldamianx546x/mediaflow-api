package com.mediaflow.api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Integer scoreId;

    @Column(name = "likes", nullable = false)
    private Integer likes; 

    @Column(name = "dislikes", nullable = false)
    private Integer dislikes;

    @Column(name = "calification", nullable = false, precision = 5, scale = 4)
    private BigDecimal calification;

    @Column(name = "views", nullable = false)
    private Integer views;

    @Column(name = "impact", nullable = false, precision = 10, scale = 4)
    private BigDecimal impact;

    // CAMBIO AQUÍ: Agregar @JoinColumn para establecer Score como dueño
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", referencedColumnName = "content_id")
    private Content content;
}