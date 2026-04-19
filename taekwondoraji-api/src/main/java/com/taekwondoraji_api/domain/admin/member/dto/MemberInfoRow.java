package com.taekwondoraji_api.domain.admin.member.dto;

import java.time.LocalDateTime;

public record MemberInfoRow(
        Integer memberId,
        String memberName,
        String loginId,
        String phoneNumber,
        String gymName,
        String memberRole,
        String memberStatus,
        LocalDateTime createdAt
) {
}
