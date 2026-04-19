package com.taekwondoraji_api.domain.admin.goal.dto;

import java.time.LocalDateTime;

public record GoalInfoRow(
        Integer goalId,
        String name,
        String description,
        String link,
        String category,
        Integer point,
        String goalSource,
        LocalDateTime createdAt
) {
}
