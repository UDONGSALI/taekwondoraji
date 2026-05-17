package com.taekwondoraji_api.domain.pointitem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "gym_point_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GymPointItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_point_item_id")
    private Integer gymPointItemId;

    @Column(name = "gym_id", nullable = false)
    private Integer gymId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "link", length = 255)
    private String link;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status", nullable = false)
    private PointItemStatus itemStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public GymPointItem(
            Integer gymId,
            String name,
            String description,
            String link,
            Integer point,
            PointItemStatus itemStatus
    ) {
        this.gymId = gymId;
        this.name = name;
        this.description = description;
        this.link = link;
        this.point = point;
        this.itemStatus = itemStatus;
    }

    public void updateItemStatus(PointItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
