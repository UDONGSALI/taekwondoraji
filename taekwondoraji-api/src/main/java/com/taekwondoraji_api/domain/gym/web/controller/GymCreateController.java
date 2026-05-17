package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.domain.auth.web.AuthSession;
import com.taekwondoraji_api.domain.gym.web.dto.GymCreateForm;
import com.taekwondoraji_api.domain.gym.web.service.GymCreateService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym/create")
public class GymCreateController {

    private final GymCreateService gymCreateService;

    @GetMapping
    public String createForm(Model model) {
        model.addAttribute("gymCreateForm", new GymCreateForm());
        return "gym/create";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("gymCreateForm") GymCreateForm form,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createError", "\uD544\uC218 \uC815\uBCF4\uB97C \uD655\uC778\uD574 \uC8FC\uC138\uC694.");
            return "gym/create";
        }

        try {
            Integer memberId = (Integer) session.getAttribute(AuthSession.LOGIN_MEMBER_ID);
            Integer gymId = gymCreateService.createGym(memberId, form);
            return "redirect:/gym/" + gymId + "/member/info";
        } catch (BusinessException exception) {
            model.addAttribute("createError", "\uB3C4\uC7A5 \uC815\uBCF4\uB97C \uD655\uC778\uD574 \uC8FC\uC138\uC694. \uC0AC\uC5C5\uC790\uBC88\uD638\uAC00 \uC911\uBCF5\uB418\uC5C8\uC744 \uC218 \uC788\uC2B5\uB2C8\uB2E4.");
            return "gym/create";
        }
    }
}
