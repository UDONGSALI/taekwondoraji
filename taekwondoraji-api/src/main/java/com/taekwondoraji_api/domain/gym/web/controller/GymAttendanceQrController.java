package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.domain.attendance.service.AttendanceQrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gym/api/{gymId}/attendance")
public class GymAttendanceQrController {

    private final AttendanceQrService attendanceQrService;

    @GetMapping(value = "/qr.svg", produces = "image/svg+xml")
    public String getTodayQr(@PathVariable Integer gymId) {
        return attendanceQrService.createTodayQrSvg(gymId);
    }
}
