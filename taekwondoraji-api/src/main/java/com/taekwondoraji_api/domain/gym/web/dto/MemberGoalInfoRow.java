package com.taekwondoraji_api.domain.gym.web.dto;

import java.time.LocalDateTime;

public record MemberGoalInfoRow(
        Integer memberGoalId,
        Integer memberId,
        String memberName,
        String memberRole,
        Integer goalId,
        String goalName,
        String category,
        Integer point,
        String goalStatusCode,
        String goalStatus,
        LocalDateTime completedAt,
        boolean completable
) {
}
