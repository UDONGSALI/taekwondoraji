package com.taekwondoraji_api.domain.gym.web.dto;

import java.time.LocalDateTime;

public record PointItemInfoRow(
        int no,
        Integer gymPointItemId,
        String name,
        String description,
        String link,
        Integer point,
        String itemStatusCode,
        String itemStatus,
        String imageUrl,
        LocalDateTime createdAt
) {
}
