package com.taekwondoraji_api.domain.gym.web.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberBeltUpdateRequest(
        @NotBlank String beltName
) {
}
