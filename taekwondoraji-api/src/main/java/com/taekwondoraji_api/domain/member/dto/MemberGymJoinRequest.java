package com.taekwondoraji_api.domain.member.dto;

import jakarta.validation.constraints.NotNull;

public record MemberGymJoinRequest(
        @NotNull(message = "도장 ID는 필수입니다.")
        Integer gymId
) {
}
