package com.taekwondoraji_api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(name = "created_dt", nullable = false, updatable = false)
    private LocalDateTime createdDt;

    @UpdateTimestamp
    @Column(name = "updated_dt", nullable = false)
    private LocalDateTime updatedDt;
}
