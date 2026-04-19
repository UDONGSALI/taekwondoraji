package com.taekwondoraji_api.domain.admin.gym.dto;

public record GymInfoSummary(
        int totalCount,
        int waitCount,
        int activeCount,
        int stopCount
) {
}
