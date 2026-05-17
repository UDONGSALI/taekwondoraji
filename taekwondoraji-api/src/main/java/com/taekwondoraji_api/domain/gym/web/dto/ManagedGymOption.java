package com.taekwondoraji_api.domain.gym.web.dto;

public record ManagedGymOption(
        Integer gymId,
        String gymName,
        String memberRole,
        String memberStatus
) {
}
