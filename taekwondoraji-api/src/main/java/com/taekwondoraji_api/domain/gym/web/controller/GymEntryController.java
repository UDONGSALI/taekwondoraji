package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.domain.auth.web.AuthSession;
import com.taekwondoraji_api.domain.gym.web.dto.ManagedGymOption;
import com.taekwondoraji_api.domain.gym.web.service.GymAccessService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym")
public class GymEntryController {

    private final GymAccessService gymAccessService;

    @GetMapping
    public String entry(HttpSession session) {
        return "redirect:/gym/select";
    }

    @GetMapping("/select")
    public String select(
            @RequestParam(required = false) String error,
            HttpSession session,
            Model model
    ) {
        Integer memberId = (Integer) session.getAttribute(AuthSession.LOGIN_MEMBER_ID);
        model.addAttribute("loginMemberName", session.getAttribute(AuthSession.LOGIN_MEMBER_NAME));
        model.addAttribute("managedGyms", gymAccessService.findManagedGyms(memberId));
        model.addAttribute("accessError", "forbidden".equals(error));
        return "gym/select";
    }
}
