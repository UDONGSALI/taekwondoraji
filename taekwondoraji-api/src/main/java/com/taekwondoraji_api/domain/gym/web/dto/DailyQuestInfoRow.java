package com.taekwondoraji_api.domain.gym.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DailyQuestInfoRow(
        Integer gymDailyQuestId,
        LocalDate questDate,
        String name,
        String description,
        String link,
        Integer point,
        LocalDateTime createdAt
) {
}
