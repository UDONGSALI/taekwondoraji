package com.taekwondoraji_api.domain.gym.web.dto;

public record MemberAttendanceCountRow(
        Integer memberGymMapId,
        Long attendanceCount
) {
}
