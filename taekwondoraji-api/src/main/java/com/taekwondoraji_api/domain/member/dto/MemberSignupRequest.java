package com.taekwondoraji_api.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberSignupRequest(
        @NotBlank(message = "로그인 아이디는 필수입니다.")
        @Size(max = 50, message = "로그인 아이디는 50자 이하로 입력해 주세요.")
        String loginId,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 4, max = 100, message = "비밀번호는 4자 이상 100자 이하로 입력해 주세요.")
        String loginPassword,

        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 50, message = "이름은 50자 이하로 입력해 주세요.")
        String memberName,

        Integer age,

        @Size(max = 20, message = "전화번호는 20자 이하로 입력해 주세요.")
        String phoneNumber,

        @Size(max = 10, message = "우편번호는 10자 이하로 입력해 주세요.")
        String postalCode,

        @Size(max = 255, message = "도로명 주소는 255자 이하로 입력해 주세요.")
        String addressRoad,

        @Size(max = 255, message = "상세 주소는 255자 이하로 입력해 주세요.")
        String addressDetail
) {
}
