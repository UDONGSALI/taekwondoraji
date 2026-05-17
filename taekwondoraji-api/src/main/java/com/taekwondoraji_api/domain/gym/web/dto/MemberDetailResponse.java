package com.taekwondoraji_api.domain.gym.web.dto;

import com.taekwondoraji_api.common.code.BeltCode;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberInfo;

import java.time.LocalDateTime;

public record MemberDetailResponse(
        Integer memberGymMapId,
        Integer memberId,
        String memberName,
        String phoneNumber,
        Integer age,
        String postalCode,
        String addressRoad,
        String addressDetail,
        String memberRole,
        String memberStatusCode,
        String memberStatus,
        String beltName,
        String beltLabel,
        Integer point,
        LocalDateTime createdAt
) {

    public static MemberDetailResponse from(MemberGymMap memberGymMap) {
        MemberInfo memberInfo = memberGymMap.getMemberInfo();
        return new MemberDetailResponse(
                memberGymMap.getMemberGymMapId(),
                memberInfo.getMemberId(),
                memberInfo.getMemberName(),
                memberInfo.getPhoneNumber(),
                memberInfo.getAge(),
                memberInfo.getPostalCode(),
                memberInfo.getAddressRoad(),
                memberInfo.getAddressDetail(),
                memberGymMap.getMemberRole().getLabel(),
                memberGymMap.getMemberStatus().name(),
                memberGymMap.getMemberStatus().getLabel(),
                memberGymMap.getBeltName(),
                BeltCode.label(memberGymMap.getBeltName()),
                memberGymMap.getPoint(),
                memberGymMap.getCreatedAt()
        );
    }
}
