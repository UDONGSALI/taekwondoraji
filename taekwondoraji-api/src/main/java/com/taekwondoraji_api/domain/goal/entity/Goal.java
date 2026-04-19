package com.taekwondoraji_api.domain.goal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "goal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Integer goalId;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "link", length = 255)
    private String link;

    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_source", nullable = false)
    private GoalSource goalSource;

    @Column(name = "created_gym_id")
    private Integer createdGymId;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    public Goal(
            String name,
            String description,
            String link,
            String category,
            Integer point,
            GoalSource goalSource,
            Integer createdGymId
    ) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.category = category;
        this.point = point;
        this.goalSource = goalSource;
        this.createdGymId = createdGymId;
    }
}
