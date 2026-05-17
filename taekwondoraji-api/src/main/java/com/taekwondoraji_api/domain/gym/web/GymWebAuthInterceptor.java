package com.taekwondoraji_api.domain.gym.web;

import com.taekwondoraji_api.domain.auth.web.AuthSession;
import com.taekwondoraji_api.domain.gym.web.service.GymAccessService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GymWebAuthInterceptor implements HandlerInterceptor {

    private final GymAccessService gymAccessService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        Integer memberId = session == null ? null : (Integer) session.getAttribute(AuthSession.LOGIN_MEMBER_ID);

        if (memberId == null) {
            String redirectUri = request.getRequestURI();
            String queryString = request.getQueryString();
            if (queryString != null && !queryString.isBlank()) {
                redirectUri += "?" + queryString;
            }
            response.sendRedirect("/login?redirectUri=" + UriUtils.encode(redirectUri, StandardCharsets.UTF_8));
            return false;
        }

        Integer gymId = extractGymId(request.getRequestURI());
        if (gymId != null && !gymAccessService.canManageGym(memberId, gymId)) {
            response.sendRedirect("/gym/select?error=forbidden");
            return false;
        }

        return true;
    }

    private Integer extractGymId(String requestUri) {
        String[] paths = requestUri.split("/");
        if (paths.length < 3 || !"gym".equals(paths[1])) {
            return null;
        }

        try {
            return Integer.valueOf(paths[2]);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
