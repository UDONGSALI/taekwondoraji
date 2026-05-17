package com.taekwondoraji_api.domain.gym.web.dto;

public record MemberAttendanceRankRow(
        int rank,
        String memberName,
        String memberRole,
        int attendanceCount,
        String attendanceRateLabel
) {
}
