package com.taekwondoraji_api.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberProfileUpdateRequest(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 50, message = "이름은 50자 이하로 입력해 주세요.")
        String memberName,

        Integer age,

        @Size(max = 20, message = "전화번호는 20자 이하로 입력해 주세요.")
        String phoneNumber,

        @Size(max = 100, message = "좌우명은 100자 이하로 입력해 주세요.")
        String motto
) {
}
