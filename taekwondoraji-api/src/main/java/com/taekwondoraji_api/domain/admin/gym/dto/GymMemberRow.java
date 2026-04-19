package com.taekwondoraji_api.domain.admin.gym.dto;

public record GymMemberRow(
        Integer memberId,
        String memberName,
        String memberRole,
        String memberStatus,
        String phoneNumber
) {
}
