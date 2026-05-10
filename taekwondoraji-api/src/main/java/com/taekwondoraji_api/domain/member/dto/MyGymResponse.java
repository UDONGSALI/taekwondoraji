package com.taekwondoraji_api.domain.member.dto;

import com.taekwondoraji_api.domain.gym.entity.GymInfo;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record MyGymResponse(
        Integer memberGymMapId,
        Integer gymId,
        String gymName,
        String addressRoad,
        String addressDetail,
        String phoneNumber,
        Integer memberAge,
        String memberPhoneNumber,
        String memberRole,
        String memberStatus,
        String beltName,
        Integer point,
        String joinedDate
) {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static MyGymResponse from(MemberGymMap memberGymMap) {
        GymInfo gym = memberGymMap.getGym();
        MemberInfo memberInfo = memberGymMap.getMemberInfo();
        return new MyGymResponse(
                memberGymMap.getMemberGymMapId(),
                gym.getGymId(),
                gym.getGymName(),
                gym.getAddressRoad(),
                gym.getAddressDetail(),
                gym.getPhoneNumber(),
                memberInfo.getAge(),
                memberInfo.getPhoneNumber(),
                memberGymMap.getMemberRole().name(),
                memberGymMap.getMemberStatus().name(),
                memberGymMap.getBeltName(),
                memberGymMap.getPoint(),
                formatDate(memberGymMap.getCreatedAt())
        );
    }

    private static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.format(DATE_FORMATTER);
    }
}
