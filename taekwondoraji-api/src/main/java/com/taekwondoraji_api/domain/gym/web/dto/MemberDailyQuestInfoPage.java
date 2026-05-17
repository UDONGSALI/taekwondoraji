package com.taekwondoraji_api.domain.gym.web.dto;

import java.time.LocalDate;
import java.util.List;

public record MemberDailyQuestInfoPage(
        LocalDate questDate,
        List<DailyQuestInfoRow> quests,
        List<MemberDailyQuestMemberRow> members
) {
}
