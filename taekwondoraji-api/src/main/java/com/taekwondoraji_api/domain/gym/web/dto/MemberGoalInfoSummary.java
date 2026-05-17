package com.taekwondoraji_api.domain.gym.web.dto;

public record MemberGoalInfoSummary(
        int totalCount,
        int progressCount,
        int completeCount,
        int cancelCount
) {
}
