package com.taekwondoraji_api.domain.gym.web.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AttendanceDateRequest(
        @NotNull
        LocalDate attendanceDate
) {
}
