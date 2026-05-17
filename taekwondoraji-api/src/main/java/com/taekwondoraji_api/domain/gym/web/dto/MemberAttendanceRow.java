package com.taekwondoraji_api.domain.gym.web.dto;

public record MemberAttendanceRow(
        int no,
        Integer memberGymMapId,
        String memberName,
        String memberRole,
        String memberStatus,
        String beltLabel,
        int attendanceCount,
        String attendanceRateLabel
) {
}
