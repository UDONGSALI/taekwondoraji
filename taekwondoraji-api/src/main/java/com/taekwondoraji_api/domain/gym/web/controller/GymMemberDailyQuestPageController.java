package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.domain.gym.web.dto.MemberDailyQuestInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.MemberDailyQuestParam;
import com.taekwondoraji_api.domain.gym.web.service.GymMemberDailyQuestCommandService;
import com.taekwondoraji_api.domain.gym.web.service.GymMemberDailyQuestPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym/{gymId}/member-daily-quest")
public class GymMemberDailyQuestPageController {

    private final GymMemberDailyQuestPageService gymMemberDailyQuestPageService;
    private final GymMemberDailyQuestCommandService gymMemberDailyQuestCommandService;

    @GetMapping("/info")
    public String info(
            @PathVariable Integer gymId,
            @ModelAttribute("param") MemberDailyQuestParam param,
            Model model
    ) {
        MemberDailyQuestInfoPage memberDailyQuestInfo = gymMemberDailyQuestPageService
                .getMemberDailyQuestInfoPage(gymId, param);

        model.addAttribute("gymId", gymId);
        model.addAttribute("memberDailyQuestInfo", memberDailyQuestInfo);
        return "gym/member-daily-quest/info";
    }

    @PostMapping("/{memberDailyQuestId}/complete")
    public String complete(
            @PathVariable Integer gymId,
            @PathVariable Integer memberDailyQuestId,
            @RequestParam(required = false) LocalDate questDate,
            RedirectAttributes redirectAttributes
    ) {
        gymMemberDailyQuestCommandService.completeMemberDailyQuest(gymId, memberDailyQuestId);
        redirectAttributes.addFlashAttribute("toastMessage", "회원 일퀘가 완료되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");

        String redirectUrl = "redirect:/gym/" + gymId + "/member-daily-quest/info";
        if (questDate != null) {
            redirectUrl += "?questDate=" + questDate;
        }
        return redirectUrl;
    }
}
