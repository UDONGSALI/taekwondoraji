package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.domain.gym.web.dto.MemberAttendanceInfoPage;
import com.taekwondoraji_api.domain.gym.web.service.GymAttendancePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym/{gymId}/attendance")
public class GymAttendancePageController {

    private final GymAttendancePageService gymAttendancePageService;

    @GetMapping("/info")
    public String info(
            @PathVariable Integer gymId,
            @RequestParam(required = false) String month,
            Model model
    ) {
        MemberAttendanceInfoPage attendanceInfo = gymAttendancePageService.getAttendanceInfoPage(gymId, month);

        model.addAttribute("gymId", gymId);
        model.addAttribute("attendanceInfo", attendanceInfo);
        return "gym/attendance/info";
    }
}
