package com.taekwondoraji_api.domain.admin.goal.controller;

import com.taekwondoraji_api.domain.admin.goal.dto.GoalCreateForm;
import com.taekwondoraji_api.domain.admin.goal.dto.GoalInfoPage;
import com.taekwondoraji_api.domain.admin.goal.service.GoalCommandService;
import com.taekwondoraji_api.domain.admin.goal.service.GoalPageService;
import com.taekwondoraji_api.domain.goal.dto.GoalCategory;
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
@RequestMapping("/admin/goal")
public class GoalPageController {

    private final GoalPageService goalPageService;
    private final GoalCommandService goalCommandService;

    @GetMapping("/info")
    public String info(Model model) {
        if (!model.containsAttribute("goalCreateForm")) {
            model.addAttribute("goalCreateForm", new GoalCreateForm());
        }

        populateModel(model);
        return "admin/goal/info";
    }

    @PostMapping
    public String createGoal(
            @Valid @ModelAttribute("goalCreateForm") GoalCreateForm goalCreateForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateModel(model);
            return "admin/goal/info";
        }

        goalCommandService.createGoal(goalCreateForm);
        redirectAttributes.addFlashAttribute("toastMessage", "목표가 등록되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/admin/goal/info";
    }

    @PostMapping("/{goalId}/delete")
    public String deleteGoal(@PathVariable Integer goalId, RedirectAttributes redirectAttributes) {
        goalCommandService.deleteGoal(goalId);
        redirectAttributes.addFlashAttribute("toastMessage", "목표가 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/admin/goal/info";
    }

    private void populateModel(Model model) {
        GoalInfoPage goalInfo = goalPageService.getGoalInfoPage();

        model.addAttribute("goalInfo", goalInfo);
        model.addAttribute("goalCategories", GoalCategory.values());
    }
}
