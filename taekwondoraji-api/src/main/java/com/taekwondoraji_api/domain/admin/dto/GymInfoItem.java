package com.taekwondoraji_api.domain.admin.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GymInfoItem(
        Long gymId,
        String gymName,
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
