package com.taekwondoraji_api.domain.admin.controller;

import com.taekwondoraji_api.domain.admin.dto.AdminDashboardResponse;
import com.taekwondoraji_api.domain.admin.dto.AdminGymInfoResponse;
import com.taekwondoraji_api.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final AdminService adminService;

    @GetMapping
    public String dashboard(Model model) {
        AdminDashboardResponse dashboard = adminService.getDashboard();

        model.addAttribute("dashboard", dashboard);
        return "admin/dashboard";
    }

    @GetMapping("/gyminfo")
    public String gymInfo(Model model) {
        AdminGymInfoResponse gymInfo = adminService.getGymInfoList();

        model.addAttribute("gymInfo", gymInfo);
        return "admin/gyminfo";
    }
}
