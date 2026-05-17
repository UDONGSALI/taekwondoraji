package com.taekwondoraji_api.domain.gym.web.dto;

import java.util.List;

public record MemberInfoPage(
        List<MemberInfoRow> list,
        MemberInfoSummary summary
) {
}
