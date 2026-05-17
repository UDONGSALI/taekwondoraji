package com.taekwondoraji_api.domain.gym.web.dto;

import java.util.List;

public record GoalInfoPage(
        List<GoalInfoRow> list,
        GoalInfoSummary summary
) {
}
