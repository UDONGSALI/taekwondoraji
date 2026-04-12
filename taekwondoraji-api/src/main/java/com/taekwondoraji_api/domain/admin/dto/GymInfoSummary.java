package com.taekwondoraji_api.domain.admin.dto;

public record GymInfoSummary(
        int totalCount,
        int waitCount,
        int activeCount,
        int stopCount
) {
}
