package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.common.code.BeltCode;
import com.taekwondoraji_api.domain.gym.web.dto.MemberInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.MemberInfoParam;
import com.taekwondoraji_api.domain.gym.web.service.GymMemberPageService;
import com.taekwondoraji_api.domain.member.entity.MemberRole;
import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym/{gymId}/member")
public class GymMemberPageController {

    private final GymMemberPageService gymMemberPageService;

    @GetMapping("/info")
    public String info(
            @PathVariable Integer gymId,
            @ModelAttribute("param") MemberInfoParam param,
            Model model
    ) {
        MemberInfoPage memberInfo = gymMemberPageService.getMemberInfoPage(gymId, param);

        model.addAttribute("gymId", gymId);
        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("memberRoles", MemberRole.values());
        model.addAttribute("memberStatuses", MemberStatus.values());
        model.addAttribute("beltOptions", BeltCode.options());
        return "gym/member/info";
    }
}
