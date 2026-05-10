package com.taekwondoraji_api.domain.goal.dto;

import com.taekwondoraji_api.domain.goal.entity.Goal;

public record GoalListResponse(
        Integer goalId,
        String name,
        String description,
        String link,
        String category,
        Integer point
) {

    public static GoalListResponse from(Goal goal) {
        return new GoalListResponse(
                goal.getGoalId(),
                goal.getName(),
                goal.getDescription(),
                goal.getLink(),
                goal.getCategory(),
                goal.getPoint()
        );
    }
}
