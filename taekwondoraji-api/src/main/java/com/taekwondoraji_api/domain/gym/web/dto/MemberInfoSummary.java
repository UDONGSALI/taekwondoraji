package com.taekwondoraji_api.domain.gym.web.dto;

public record MemberInfoSummary(
        int totalCount,
        int waitCount,
        int activeCount,
        int stopCount
) {
}
