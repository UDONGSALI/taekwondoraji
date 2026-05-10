package com.taekwondoraji_api.domain.attendance.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.attendance.dto.AttendanceResponse;
import com.taekwondoraji_api.domain.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member-gym-maps")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/{memberGymMapId}/attendances")
    public ApiResponse<List<AttendanceResponse>> getMonthlyAttendances(
            @PathVariable Integer memberGymMapId,
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        return ApiResponse.ok(attendanceService.getMonthlyAttendances(memberGymMapId, year, month));
    }

    @PostMapping("/{memberGymMapId}/attendances/today")
    public ApiResponse<AttendanceResponse> attendToday(@PathVariable Integer memberGymMapId) {
        return ApiResponse.ok(attendanceService.attendToday(memberGymMapId));
    }
}
