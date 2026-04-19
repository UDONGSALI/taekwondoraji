package com.taekwondoraji_api.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    wait("대기"),
    active("서비스 중"),
    stop("중지");

    private final String label;
}
