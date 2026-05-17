package com.taekwondoraji_api.domain.member.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.member.dto.MemberGymJoinRequest;
import com.taekwondoraji_api.domain.member.dto.MemberLoginRequest;
import com.taekwondoraji_api.domain.member.dto.MemberLoginResponse;
import com.taekwondoraji_api.domain.member.dto.MemberProfileResponse;
import com.taekwondoraji_api.domain.member.dto.MemberProfileUpdateRequest;
import com.taekwondoraji_api.domain.member.dto.MemberResponse;
import com.taekwondoraji_api.domain.member.dto.MemberSignupRequest;
import com.taekwondoraji_api.domain.member.dto.MemberSignupResponse;
import com.taekwondoraji_api.domain.member.dto.MyGymResponse;
import com.taekwondoraji_api.domain.member.service.MemberGymService;
import com.taekwondoraji_api.domain.member.service.MemberLoginService;
import com.taekwondoraji_api.domain.member.service.MemberProfileService;
import com.taekwondoraji_api.domain.member.service.MemberService;
import com.taekwondoraji_api.domain.member.service.MemberSignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberSignupService memberSignupService;
    private final MemberLoginService memberLoginService;
    private final MemberGymService memberGymService;
    private final MemberProfileService memberProfileService;

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

    @PatchMapping("/{memberId}/profile")
    public ApiResponse<MemberProfileResponse> updateProfile(
            @PathVariable Integer memberId,
            @Valid @RequestBody MemberProfileUpdateRequest request
    ) {
        return ApiResponse.ok(memberProfileService.updateProfile(memberId, request));
    }

    @PostMapping("/{memberId}/profile-image")
    public ApiResponse<MemberProfileResponse> updateProfileImage(
            @PathVariable Integer memberId,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {
        return ApiResponse.ok(memberProfileService.updateProfileImage(memberId, imageFile));
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
