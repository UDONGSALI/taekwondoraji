package com.taekwondoraji_api.domain.admin.member.dto;

public record MemberInfoSummary(
        int totalCount,
        int waitCount,
        int activeCount,
        int stopCount
) {
}
