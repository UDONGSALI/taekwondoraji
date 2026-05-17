package com.taekwondoraji_api.domain.gym.web.dto;

public record MemberAttendanceSummary(
        int memberCount,
        int totalAttendanceCount,
        String averageAttendanceLabel,
        String averageRateLabel,
        int attendanceDays
) {
}
