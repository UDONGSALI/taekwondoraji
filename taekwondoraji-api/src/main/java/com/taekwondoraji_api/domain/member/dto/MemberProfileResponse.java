package com.taekwondoraji_api.domain.member.dto;

import com.taekwondoraji_api.domain.member.entity.MemberInfo;

public record MemberProfileResponse(
        Integer memberId,
        String memberName,
        Integer age,
        String phoneNumber,
        String motto,
        String profileImageUrl
) {

    public static MemberProfileResponse from(MemberInfo memberInfo, String profileImageUrl) {
        return new MemberProfileResponse(
                memberInfo.getMemberId(),
                memberInfo.getMemberName(),
                memberInfo.getAge(),
                memberInfo.getPhoneNumber(),
                memberInfo.getMotto(),
                profileImageUrl
        );
    }
}
