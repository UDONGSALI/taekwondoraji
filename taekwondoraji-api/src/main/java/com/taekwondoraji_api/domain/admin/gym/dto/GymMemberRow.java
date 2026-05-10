package com.taekwondoraji_api.domain.admin.gym.dto;

public record GymMemberRow(
        Integer memberId,
        String memberName,
        String memberRole,
        String memberStatusCode,
        String memberStatus,
        String phoneNumber
) {
}
