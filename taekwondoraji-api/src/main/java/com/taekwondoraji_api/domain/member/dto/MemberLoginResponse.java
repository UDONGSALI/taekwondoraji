package com.taekwondoraji_api.domain.member.dto;

import com.taekwondoraji_api.domain.member.entity.MemberInfo;

public record MemberLoginResponse(
        Integer memberId,
        String loginId,
        String memberName
) {

    public static MemberLoginResponse from(MemberInfo memberInfo) {
        return new MemberLoginResponse(
                memberInfo.getMemberId(),
                memberInfo.getLoginId(),
                memberInfo.getMemberName()
        );
    }
}
