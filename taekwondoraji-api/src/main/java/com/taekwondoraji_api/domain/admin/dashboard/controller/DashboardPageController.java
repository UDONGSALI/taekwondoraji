package com.taekwondoraji_api.domain.admin.dashboard.controller;

import com.taekwondoraji_api.domain.admin.dashboard.dto.DashboardPage;
import com.taekwondoraji_api.domain.admin.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class DashboardPageController {

    private final DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model) {
        DashboardPage dashboard = dashboardService.getDashboardPage();

        model.addAttribute("dashboard", dashboard);
        return "admin/dashboard";
    }
}
