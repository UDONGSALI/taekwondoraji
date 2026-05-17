package com.taekwondoraji_api.domain.dailyquest.dto;

import com.taekwondoraji_api.domain.dailyquest.entity.GymDailyQuest;
import com.taekwondoraji_api.domain.dailyquest.entity.MemberDailyQuest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DailyQuestListResponse(
        Integer dailyQuestId,
        Integer memberDailyQuestId,
        LocalDate questDate,
        String name,
        String description,
        String link,
        Integer point,
        String questStatus,
        LocalDateTime completedAt
) {

    public static DailyQuestListResponse waiting(GymDailyQuest dailyQuest) {
        return new DailyQuestListResponse(
                dailyQuest.getGymDailyQuestId(),
                null,
                dailyQuest.getQuestDate(),
                dailyQuest.getName(),
                dailyQuest.getDescription(),
                dailyQuest.getLink(),
                dailyQuest.getPoint(),
                "waiting",
                null
        );
    }

    public static DailyQuestListResponse from(MemberDailyQuest memberDailyQuest) {
        GymDailyQuest dailyQuest = memberDailyQuest.getGymDailyQuest();

        return new DailyQuestListResponse(
                dailyQuest.getGymDailyQuestId(),
                memberDailyQuest.getMemberDailyQuestId(),
                dailyQuest.getQuestDate(),
                dailyQuest.getName(),
                dailyQuest.getDescription(),
                dailyQuest.getLink(),
                dailyQuest.getPoint(),
                memberDailyQuest.getQuestStatus().name(),
                memberDailyQuest.getCompletedAt()
        );
    }
}
