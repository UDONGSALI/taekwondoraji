package com.taekwondoraji_api.domain.gym.web.dto;

import java.util.List;

public record DailyQuestInfoPage(
        List<DailyQuestInfoRow> list,
        DailyQuestInfoSummary summary
) {
}
