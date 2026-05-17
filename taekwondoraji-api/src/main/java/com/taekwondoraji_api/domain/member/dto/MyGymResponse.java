package com.taekwondoraji_api.domain.member.dto;

import com.taekwondoraji_api.common.code.BeltCode;
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
        String memberName,
        Integer memberAge,
        String memberPhoneNumber,
        String memberMotto,
        String memberProfileImageUrl,
        String memberRole,
        String memberStatus,
        String beltName,
        String beltLabel,
        Integer point,
        String joinedDate
) {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static MyGymResponse from(MemberGymMap memberGymMap, String memberProfileImageUrl) {
        GymInfo gym = memberGymMap.getGym();
        MemberInfo memberInfo = memberGymMap.getMemberInfo();
        return new MyGymResponse(
                memberGymMap.getMemberGymMapId(),
                gym.getGymId(),
                gym.getGymName(),
                gym.getAddressRoad(),
                gym.getAddressDetail(),
                gym.getPhoneNumber(),
                memberInfo.getMemberName(),
                memberInfo.getAge(),
                memberInfo.getPhoneNumber(),
                memberInfo.getMotto(),
                memberProfileImageUrl,
                memberGymMap.getMemberRole().name(),
                memberGymMap.getMemberStatus().name(),
                memberGymMap.getBeltName(),
                BeltCode.label(memberGymMap.getBeltName()),
                memberGymMap.getPoint(),
                formatDate(memberGymMap.getCreatedAt())
        );
    }

    public static MyGymResponse from(MemberGymMap memberGymMap) {
        return from(memberGymMap, null);
    }

    private static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.format(DATE_FORMATTER);
    }
}
