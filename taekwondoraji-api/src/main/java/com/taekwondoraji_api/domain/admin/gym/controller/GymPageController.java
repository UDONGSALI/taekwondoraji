package com.taekwondoraji_api.domain.admin.gym.controller;

import com.taekwondoraji_api.domain.admin.gym.dto.GymInfoParam;
import com.taekwondoraji_api.domain.admin.gym.dto.GymDetailPage;
import com.taekwondoraji_api.domain.admin.gym.dto.GymInfoPage;
import com.taekwondoraji_api.domain.admin.gym.service.GymPageService;
import com.taekwondoraji_api.domain.gym.entity.GymServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/gym")
public class GymPageController {

    private final GymPageService gymPageService;

    @GetMapping("/info")
    public String info(@ModelAttribute("param") GymInfoParam param, Model model) {
        GymInfoPage gymInfo = gymPageService.getGymInfoPage(param);

        model.addAttribute("gymInfo", gymInfo);
        model.addAttribute("serviceStatuses", GymServiceStatus.values());
        model.addAttribute("regionOptions", gymPageService.getRegionOptions());
        return "admin/gym/info";
    }

    @GetMapping("/detail/{gymId}")
    public String detail(@PathVariable Integer gymId, Model model) {
        GymDetailPage gymDetail = gymPageService.getGymDetailPage(gymId);

        model.addAttribute("gymDetail", gymDetail);
        model.addAttribute("serviceStatuses", GymServiceStatus.values());

        return "admin/gym/detail";
    }
}
