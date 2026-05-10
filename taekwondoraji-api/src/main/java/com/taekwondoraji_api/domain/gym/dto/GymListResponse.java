package com.taekwondoraji_api.domain.gym.dto;

import com.taekwondoraji_api.domain.gym.entity.GymInfo;

public record GymListResponse(
        Integer gymId,
        String gymName,
        String ownerName,
        String phoneNumber,
        String addressRoad,
        String addressDetail,
        String regionCode,
        String serviceStatus
) {

    public static GymListResponse from(GymInfo gymInfo) {
        return new GymListResponse(
                gymInfo.getGymId(),
                gymInfo.getGymName(),
                gymInfo.getOwnerName(),
                gymInfo.getPhoneNumber(),
                gymInfo.getAddressRoad(),
                gymInfo.getAddressDetail(),
                gymInfo.getRegionCode(),
                gymInfo.getServiceStatus().name()
        );
    }
}
