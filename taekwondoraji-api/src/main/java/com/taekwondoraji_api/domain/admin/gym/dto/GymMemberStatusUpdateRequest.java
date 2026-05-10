package com.taekwondoraji_api.domain.admin.gym.dto;

import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import jakarta.validation.constraints.NotNull;

public record GymMemberStatusUpdateRequest(
        @NotNull(message = "관원 상태는 필수입니다.")
        MemberStatus memberStatus
) {
}
