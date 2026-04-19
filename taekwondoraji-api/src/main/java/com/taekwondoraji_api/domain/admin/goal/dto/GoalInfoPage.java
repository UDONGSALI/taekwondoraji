package com.taekwondoraji_api.domain.admin.goal.dto;

import java.util.List;

public record GoalInfoPage(
        List<GoalInfoRow> list,
        GoalInfoSummary summary
) {
}
