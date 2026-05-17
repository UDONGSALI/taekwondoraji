package com.taekwondoraji_api.domain.auth.web;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.member.dto.MemberLoginResponse;
import com.taekwondoraji_api.domain.member.dto.MemberSignupRequest;
import com.taekwondoraji_api.domain.member.entity.MemberInfo;
import com.taekwondoraji_api.domain.member.repository.MemberInfoRepository;
import com.taekwondoraji_api.domain.member.service.MemberSignupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberSignupService memberSignupService;

    @GetMapping("/login")
    public String loginForm(
            @RequestParam(required = false) String redirectUri,
            Model model
    ) {
        WebLoginForm form = new WebLoginForm();
        form.setRedirectUri(safeRedirectUri(redirectUri));
        model.addAttribute("loginForm", form);
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") WebLoginForm form,
            BindingResult bindingResult,
            HttpServletRequest request,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginError", "\uC544\uC774\uB514\uB97C \uC785\uB825\uD574 \uC8FC\uC138\uC694.");
            return "auth/login";
        }

        try {
            MemberInfo memberInfo = memberInfoRepository.findByLoginId(form.getLoginId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_ID_NOT_FOUND));
            MemberLoginResponse response = MemberLoginResponse.from(memberInfo);
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute(AuthSession.LOGIN_MEMBER_ID, response.memberId());
            session.setAttribute(AuthSession.LOGIN_MEMBER_NAME, response.memberName());

            return "redirect:/gym";
        } catch (BusinessException exception) {
            model.addAttribute("loginError", loginErrorMessage(exception));
            return "auth/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupForm", new WebSignupForm());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(
            @Valid @ModelAttribute("signupForm") WebSignupForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("signupError", "\uD544\uC218 \uC815\uBCF4\uB97C \uD655\uC778\uD574 \uC8FC\uC138\uC694.");
            return "auth/signup";
        }

        try {
            memberSignupService.signup(new MemberSignupRequest(
                    form.getLoginId(),
                    form.getLoginPassword(),
                    form.getMemberName(),
                    form.getAge(),
                    form.getPhoneNumber(),
                    null,
                    null,
                    null
            ));
            redirectAttributes.addFlashAttribute("signupMessage", "\uC544\uC774\uB514\uAC00 \uC0DD\uC131\uB418\uC5C8\uC2B5\uB2C8\uB2E4. \uB85C\uADF8\uC778\uD574 \uC8FC\uC138\uC694.");
            return "redirect:/login";
        } catch (BusinessException exception) {
            model.addAttribute("signupError", "\uC774\uBBF8 \uC0AC\uC6A9 \uC911\uC778 \uB85C\uADF8\uC778 \uC544\uC774\uB514\uC785\uB2C8\uB2E4.");
            return "auth/signup";
        }
    }

    private String safeRedirectUri(String redirectUri) {
        if (redirectUri == null || redirectUri.isBlank() || !redirectUri.startsWith("/") || redirectUri.startsWith("//")) {
            return null;
        }

        return redirectUri;
    }

    private String loginErrorMessage(BusinessException exception) {
        if (exception.getErrorCode() == ErrorCode.LOGIN_ID_NOT_FOUND) {
            return "\uC544\uC774\uB514\uAC00 \uC874\uC7AC\uD558\uC9C0 \uC54A\uC2B5\uB2C8\uB2E4.";
        }

        if (exception.getErrorCode() == ErrorCode.INVALID_LOGIN_PASSWORD) {
            return "\uBE44\uBC00\uBC88\uD638\uAC00 \uC77C\uCE58\uD558\uC9C0 \uC54A\uC2B5\uB2C8\uB2E4.";
        }

        return "\uB85C\uADF8\uC778 \uC815\uBCF4\uB97C \uD655\uC778\uD574 \uC8FC\uC138\uC694.";
    }
}
