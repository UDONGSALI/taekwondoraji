package com.taekwondoraji_api.domain.gym.web.dto;

public record PointItemInfoSummary(
        int itemCount,
        int holdCount,
        int usedCount,
        int cancelCount
) {
}
