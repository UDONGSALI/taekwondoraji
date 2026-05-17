package com.taekwondoraji_api.domain.gym.web.dto;

import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import jakarta.validation.constraints.NotNull;

public record MemberStatusUpdateRequest(
        @NotNull(message = "회원 상태는 필수입니다.")
        MemberStatus memberStatus
) {
}
