package com.taekwondoraji_api.domain.admin.dto;

import java.util.List;

public record AdminGymInfoResponse(
    List<GymInfoItem> list,
    GymInfoSummary summary) {
}
