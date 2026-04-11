package com.taekwondoraji_api.common.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected void markCreated() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    protected void markUpdated() {
        this.updatedAt = LocalDateTime.now();
    }
}
