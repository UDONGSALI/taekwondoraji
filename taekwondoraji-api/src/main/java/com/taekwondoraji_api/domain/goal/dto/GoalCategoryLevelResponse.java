package com.taekwondoraji_api.domain.goal.dto;

public record GoalCategoryLevelResponse(
        String category,
        Integer point,
        Integer level
) {
}
