package com.taekwondoraji_api.domain.gym.web.dto;

import java.time.LocalDateTime;

public record MemberDailyQuestMemberRow(
        int no,
        Integer memberDailyQuestId,
        String memberName,
        String memberStatus,
        String beltLabel,
        String questName,
        Integer point,
        String questStatus,
        String questStatusCode,
        LocalDateTime completedAt,
        boolean completable
) {
}
