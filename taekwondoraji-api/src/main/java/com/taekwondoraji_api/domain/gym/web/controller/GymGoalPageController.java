package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.domain.goal.dto.GoalCategory;
import com.taekwondoraji_api.domain.gym.web.dto.GoalCreateForm;
import com.taekwondoraji_api.domain.gym.web.dto.GoalInfoPage;
import com.taekwondoraji_api.domain.gym.web.service.GymGoalCommandService;
import com.taekwondoraji_api.domain.gym.web.service.GymGoalPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym/{gymId}/goal")
public class GymGoalPageController {

    private final GymGoalPageService gymGoalPageService;
    private final GymGoalCommandService gymGoalCommandService;

    @GetMapping("/info")
    public String info(@PathVariable Integer gymId, Model model) {
        if (!model.containsAttribute("goalCreateForm")) {
            model.addAttribute("goalCreateForm", new GoalCreateForm());
        }

        populateModel(gymId, model);
        return "gym/goal/info";
    }

    @PostMapping
    public String createGoal(
            @PathVariable Integer gymId,
            @Valid @ModelAttribute("goalCreateForm") GoalCreateForm goalCreateForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateModel(gymId, model);
            return "gym/goal/info";
        }

        gymGoalCommandService.createGoal(gymId, goalCreateForm);
        redirectAttributes.addFlashAttribute("toastMessage", "목표가 등록되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/gym/" + gymId + "/goal/info";
    }

    @PostMapping("/{goalId}/delete")
    public String deleteGoal(
            @PathVariable Integer gymId,
            @PathVariable Integer goalId,
            RedirectAttributes redirectAttributes
    ) {
        gymGoalCommandService.deleteGoal(gymId, goalId);
        redirectAttributes.addFlashAttribute("toastMessage", "목표가 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/gym/" + gymId + "/goal/info";
    }

    private void populateModel(Integer gymId, Model model) {
        GoalInfoPage goalInfo = gymGoalPageService.getGoalInfoPage(gymId);

        model.addAttribute("gymId", gymId);
        model.addAttribute("goalInfo", goalInfo);
        model.addAttribute("goalCategories", GoalCategory.values());
    }
}
