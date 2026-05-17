package com.taekwondoraji_api.domain.gym.web;

import com.taekwondoraji_api.domain.auth.web.AuthSession;
import com.taekwondoraji_api.domain.gym.web.service.GymAccessService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.taekwondoraji_api.domain.gym.web")
@RequiredArgsConstructor
public class GymWebModelAdvice {

    private final GymAccessService gymAccessService;

    @ModelAttribute("loginMemberName")
    public Object loginMemberName(HttpSession session) {
        return session.getAttribute(AuthSession.LOGIN_MEMBER_NAME);
    }

    @ModelAttribute("sidebarManagedGyms")
    public Object sidebarManagedGyms(HttpSession session) {
        Integer memberId = (Integer) session.getAttribute(AuthSession.LOGIN_MEMBER_ID);
        return gymAccessService.findManagedGyms(memberId);
    }
}
