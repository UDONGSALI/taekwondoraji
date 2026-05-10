package com.taekwondoraji_api.domain.attendance.dto;

import com.taekwondoraji_api.domain.attendance.entity.MemberAttendance;

public record AttendanceResponse(
        Integer attendanceId,
        String attendanceDate
) {

    public static AttendanceResponse from(MemberAttendance attendance) {
        return new AttendanceResponse(
                attendance.getAttendanceId(),
                attendance.getAttendanceDate().toString()
        );
    }
}
