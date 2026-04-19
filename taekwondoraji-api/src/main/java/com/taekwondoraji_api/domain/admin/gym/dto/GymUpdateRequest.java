package com.taekwondoraji_api.domain.admin.gym.dto;

import com.taekwondoraji_api.domain.gym.entity.GymServiceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GymUpdateRequest(
        @NotNull(message = "서비스 상태는 필수입니다.")
        GymServiceStatus serviceStatus,
        LocalDate serviceStartDate,
        LocalDate serviceEndDate
) {
}
