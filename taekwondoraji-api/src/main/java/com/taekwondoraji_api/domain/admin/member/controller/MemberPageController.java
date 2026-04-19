package com.taekwondoraji_api.domain.admin.member.controller;

import com.taekwondoraji_api.domain.admin.member.dto.MemberInfoPage;
import com.taekwondoraji_api.domain.admin.member.dto.MemberInfoParam;
import com.taekwondoraji_api.domain.admin.member.service.MemberPageService;
import com.taekwondoraji_api.domain.member.entity.MemberRole;
import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/member")
public class MemberPageController {

    private final MemberPageService memberPageService;

    @GetMapping("/info")
    public String info(@ModelAttribute("param") MemberInfoParam param, Model model) {
        MemberInfoPage memberInfo = memberPageService.getMemberInfoPage(param);

        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("memberRoles", MemberRole.values());
        model.addAttribute("memberStatuses", MemberStatus.values());
        return "admin/member/info";
    }
}
