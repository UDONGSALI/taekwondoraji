package com.taekwondoraji_api.domain.admin.gym.dto;

import java.util.List;

public record GymInfoPage(
        List<GymInfoRow> list,
        GymInfoSummary summary
) {
}
