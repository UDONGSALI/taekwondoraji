package com.taekwondoraji_api.domain.admin.gym.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GymInfoRow(
        Long gymId,
        String gymName,
        String serviceStatusCode,
        String status,
        String businessNumber,
        String ownerName,
        String phoneNumber,
        String address,
        String regionName,
        LocalDate serviceStartDate,
        LocalDate serviceEndDate,
        LocalDateTime createdDt
) {
}
