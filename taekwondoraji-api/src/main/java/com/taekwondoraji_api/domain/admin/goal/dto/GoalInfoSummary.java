package com.taekwondoraji_api.domain.admin.goal.dto;

public record GoalInfoSummary(
        int totalCount,
        int commonCount,
        int customCount
) {
}
