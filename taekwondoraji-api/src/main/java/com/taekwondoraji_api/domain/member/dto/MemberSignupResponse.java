package com.taekwondoraji_api.domain.member.dto;

import com.taekwondoraji_api.domain.member.entity.MemberInfo;

public record MemberSignupResponse(
        Integer memberId,
        String loginId,
        String memberName
) {

    public static MemberSignupResponse from(MemberInfo memberInfo) {
        return new MemberSignupResponse(
                memberInfo.getMemberId(),
                memberInfo.getLoginId(),
                memberInfo.getMemberName()
        );
    }
}
