package com.taekwondoraji_api.domain.gym.web.dto;

import java.util.List;

public record MemberGoalInfoPage(
        List<MemberGoalInfoRow> list,
        MemberGoalInfoSummary summary
) {
}
