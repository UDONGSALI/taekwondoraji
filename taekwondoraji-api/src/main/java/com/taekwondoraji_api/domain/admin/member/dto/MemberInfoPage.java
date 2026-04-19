package com.taekwondoraji_api.domain.admin.member.dto;

import java.util.List;

public record MemberInfoPage(
        List<MemberInfoRow> list,
        MemberInfoSummary summary
) {
}
