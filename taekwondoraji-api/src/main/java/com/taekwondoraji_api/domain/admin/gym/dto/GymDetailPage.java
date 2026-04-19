package com.taekwondoraji_api.domain.admin.gym.dto;

public record GymDetailPage(
    GymInfoRow gymInfo,
    GymMemberSection members
) {
}
