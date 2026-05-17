package com.taekwondoraji_api.domain.gym.web.dto;

import java.time.LocalDateTime;

public record MemberInfoRow(
        Integer memberGymMapId,
        Integer memberId,
        String memberName,
        String phoneNumber,
        String memberRole,
        String memberStatusCode,
        String memberStatus,
        String beltName,
        String beltLabel,
        Integer point,
        LocalDateTime createdAt
) {
}
