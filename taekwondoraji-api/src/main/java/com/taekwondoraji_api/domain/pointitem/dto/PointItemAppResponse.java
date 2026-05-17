package com.taekwondoraji_api.domain.pointitem.dto;

import com.taekwondoraji_api.domain.pointitem.entity.GymPointItem;
import com.taekwondoraji_api.domain.pointitem.entity.MemberPointItem;

public record PointItemAppResponse(
        Integer gymPointItemId,
        Integer memberPointItemId,
        String name,
        String description,
        String link,
        Integer point,
        String itemStatus,
        String itemStatusLabel,
        String memberItemStatus,
        String memberItemStatusLabel,
        String imageUrl
) {

    public static PointItemAppResponse from(GymPointItem pointItem, MemberPointItem memberPointItem, String imageUrl) {
        return new PointItemAppResponse(
                pointItem.getGymPointItemId(),
                memberPointItem == null ? null : memberPointItem.getMemberPointItemId(),
                pointItem.getName(),
                pointItem.getDescription(),
                pointItem.getLink(),
                pointItem.getPoint(),
                pointItem.getItemStatus().name(),
                pointItem.getItemStatus().getLabel(),
                memberPointItem == null ? null : memberPointItem.getItemStatus().name(),
                memberPointItem == null ? null : memberPointItem.getItemStatus().getLabel(),
                imageUrl
        );
    }
}
