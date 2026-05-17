package com.taekwondoraji_api.domain.gym.web.dto;

import java.time.LocalDateTime;

public record MemberPointItemRow(
        int no,
        Integer memberPointItemId,
        String memberName,
        String memberStatus,
        String beltLabel,
        String itemName,
        Integer point,
        String itemStatusCode,
        String itemStatus,
        LocalDateTime createdAt,
        LocalDateTime usedAt
) {
}
