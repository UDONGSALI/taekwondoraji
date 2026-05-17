package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.domain.gym.web.dto.MemberGoalInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.MemberGoalInfoParam;
import com.taekwondoraji_api.domain.gym.web.service.GymMemberGoalPageService;
import com.taekwondoraji_api.domain.goal.dto.GoalCategory;
import com.taekwondoraji_api.domain.goal.entity.GoalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym/{gymId}/member-goal")
public class GymMemberGoalPageController {

    private final GymMemberGoalPageService gymMemberGoalPageService;

    @GetMapping("/info")
    public String info(
            @PathVariable Integer gymId,
            @ModelAttribute("param") MemberGoalInfoParam param,
            Model model
    ) {
        if (param.getGoalStatus() == null || param.getGoalStatus().isBlank()) {
            param.setGoalStatus(GoalStatus.progress.name());
        }

        MemberGoalInfoPage memberGoalInfo = gymMemberGoalPageService.getMemberGoalInfoPage(gymId, param);

        model.addAttribute("gymId", gymId);
        model.addAttribute("memberGoalInfo", memberGoalInfo);
        model.addAttribute("goalStatuses", GoalStatus.values());
        model.addAttribute("goalCategories", GoalCategory.values());
        model.addAttribute("memberOptions", gymMemberGoalPageService.getMemberOptions(gymId));
        return "gym/member-goal/info";
    }
}
