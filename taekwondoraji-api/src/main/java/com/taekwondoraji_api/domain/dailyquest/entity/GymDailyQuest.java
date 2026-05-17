package com.taekwondoraji_api.domain.dailyquest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "gym_daily_quest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GymDailyQuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_daily_quest_id")
    private Integer gymDailyQuestId;

    @Column(name = "gym_id", nullable = false)
    private Integer gymId;

    @Column(name = "quest_date", nullable = false)
    private LocalDate questDate;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "link", length = 255)
    private String link;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public GymDailyQuest(
            Integer gymId,
            LocalDate questDate,
            String name,
            String description,
            String link,
            Integer point
    ) {
        this.gymId = gymId;
        this.questDate = questDate;
        this.name = name;
        this.description = description;
        this.link = link;
        this.point = point;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
