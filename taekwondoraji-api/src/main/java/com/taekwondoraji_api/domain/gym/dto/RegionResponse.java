package com.taekwondoraji_api.domain.gym.dto;

import com.taekwondoraji_api.common.entity.RegionCodeInfo;

public record RegionResponse(
        String regionCode,
        String regionName
) {

    public static RegionResponse from(RegionCodeInfo regionCodeInfo) {
        return new RegionResponse(
                regionCodeInfo.getRegionCode(),
                regionCodeInfo.getRegionName()
        );
    }
}
