package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.domain.gym.web.dto.DailyQuestCreateForm;
import com.taekwondoraji_api.domain.gym.web.dto.DailyQuestInfoPage;
import com.taekwondoraji_api.domain.gym.web.service.GymDailyQuestCommandService;
import com.taekwondoraji_api.domain.gym.web.service.GymDailyQuestPageService;
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
@RequestMapping("/gym/{gymId}/daily-quest")
public class GymDailyQuestPageController {

    private final GymDailyQuestPageService gymDailyQuestPageService;
    private final GymDailyQuestCommandService gymDailyQuestCommandService;

    @GetMapping("/info")
    public String info(@PathVariable Integer gymId, Model model) {
        if (!model.containsAttribute("dailyQuestCreateForm")) {
            model.addAttribute("dailyQuestCreateForm", new DailyQuestCreateForm());
        }

        populateModel(gymId, model);
        return "gym/daily-quest/info";
    }

    @PostMapping
    public String createDailyQuest(
            @PathVariable Integer gymId,
            @Valid @ModelAttribute("dailyQuestCreateForm") DailyQuestCreateForm dailyQuestCreateForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateModel(gymId, model);
            return "gym/daily-quest/info";
        }

        gymDailyQuestCommandService.createDailyQuest(gymId, dailyQuestCreateForm);
        redirectAttributes.addFlashAttribute("toastMessage", "일일 퀘스트가 등록되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/gym/" + gymId + "/daily-quest/info";
    }

    @PostMapping("/{dailyQuestId}/delete")
    public String deleteDailyQuest(
            @PathVariable Integer gymId,
            @PathVariable Integer dailyQuestId,
            RedirectAttributes redirectAttributes
    ) {
        gymDailyQuestCommandService.deleteDailyQuest(gymId, dailyQuestId);
        redirectAttributes.addFlashAttribute("toastMessage", "일일 퀘스트가 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/gym/" + gymId + "/daily-quest/info";
    }

    private void populateModel(Integer gymId, Model model) {
        DailyQuestInfoPage dailyQuestInfo = gymDailyQuestPageService.getDailyQuestInfoPage(gymId);

        model.addAttribute("gymId", gymId);
        model.addAttribute("dailyQuestInfo", dailyQuestInfo);
    }
}
