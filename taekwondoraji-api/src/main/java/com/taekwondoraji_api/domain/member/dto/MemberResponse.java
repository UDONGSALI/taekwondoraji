package com.taekwondoraji_api.domain.member.dto;

import com.taekwondoraji_api.domain.member.entity.Member;

public record MemberResponse(
        Long memberId,
        String name,
        String age,
        String email
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getSeq(),
                member.getName(),
                member.getAge(),
                member.getEmail()
        );
    }
}
