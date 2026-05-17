package com.taekwondoraji_api.domain.gym.web.dto;

import java.util.List;

public record PointItemInfoPage(
        List<PointItemInfoRow> items,
        List<MemberPointItemRow> members,
        PointItemInfoSummary summary
) {
}
