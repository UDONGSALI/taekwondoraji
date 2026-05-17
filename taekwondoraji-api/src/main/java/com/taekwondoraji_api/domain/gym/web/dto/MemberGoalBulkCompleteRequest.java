package com.taekwondoraji_api.domain.gym.web.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record MemberGoalBulkCompleteRequest(
        @NotEmpty(message = "완료 처리할 목표를 선택해주세요.")
        List<Integer> memberGoalIds
) {
}
