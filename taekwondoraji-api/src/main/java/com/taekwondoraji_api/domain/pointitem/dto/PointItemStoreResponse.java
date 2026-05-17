package com.taekwondoraji_api.domain.pointitem.dto;

import java.util.List;

public record PointItemStoreResponse(
        Integer currentPoint,
        List<PointItemAppResponse> items
) {
}
