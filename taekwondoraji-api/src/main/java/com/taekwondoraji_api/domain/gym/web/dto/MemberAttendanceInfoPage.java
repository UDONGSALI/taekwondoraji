package com.taekwondoraji_api.domain.gym.web.dto;

import java.util.List;

public record MemberAttendanceInfoPage(
        String selectedMonth,
        String maxMonth,
        String monthLabel,
        List<MemberAttendanceRow> list,
        MemberAttendanceSummary summary,
        List<MemberAttendanceRankRow> bestList,
        List<MemberAttendanceRankRow> worstList
) {
}
