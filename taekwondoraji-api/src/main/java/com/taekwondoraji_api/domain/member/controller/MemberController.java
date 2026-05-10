package com.taekwondoraji_api.domain.member.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.member.dto.MemberGymJoinRequest;
import com.taekwondoraji_api.domain.member.dto.MemberLoginRequest;
import com.taekwondoraji_api.domain.member.dto.MemberLoginResponse;
import com.taekwondoraji_api.domain.member.dto.MemberResponse;
import com.taekwondoraji_api.domain.member.dto.MemberSignupRequest;
import com.taekwondoraji_api.domain.member.dto.MemberSignupResponse;
import com.taekwondoraji_api.domain.member.dto.MyGymResponse;
import com.taekwondoraji_api.domain.member.service.MemberGymService;
import com.taekwondoraji_api.domain.member.service.MemberLoginService;
import com.taekwondoraji_api.domain.member.service.MemberService;
import com.taekwondoraji_api.domain.member.service.MemberSignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberSignupService memberSignupService;
    private final MemberLoginService memberLoginService;
    private final MemberGymService memberGymService;

    @PostMapping("/signup")
    public ApiResponse<MemberSignupResponse> signup(@Valid @RequestBody MemberSignupRequest request) {
        return ApiResponse.ok(memberSignupService.signup(request));
    }

    @PostMapping("/login")
    public ApiResponse<MemberLoginResponse> login(@Valid @RequestBody MemberLoginRequest request) {
        return ApiResponse.ok(memberLoginService.login(request));
    }

    @GetMapping("/{memberId}/gyms")
    public ApiResponse<java.util.List<MyGymResponse>> getMyGyms(@PathVariable Integer memberId) {
        return ApiResponse.ok(memberGymService.getMyGyms(memberId));
    }

    @PostMapping("/{memberId}/gyms")
    public ApiResponse<MyGymResponse> joinGym(
            @PathVariable Integer memberId,
            @Valid @RequestBody MemberGymJoinRequest request
    ) {
        return ApiResponse.ok(memberGymService.joinGym(memberId, request.gymId()));
    }

    @GetMapping("/me")
    public ApiResponse<MemberResponse> getMyInfo() {
        return ApiResponse.ok(memberService.getMyInfo());
    }

    @GetMapping("/{memberId}")
    public ApiResponse<MemberResponse> getMember(@PathVariable Long memberId) {
        return ApiResponse.ok(memberService.getMember(memberId));
    }
}
