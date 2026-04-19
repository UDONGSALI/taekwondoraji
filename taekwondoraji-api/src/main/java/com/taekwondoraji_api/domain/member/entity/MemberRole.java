package com.taekwondoraji_api.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    master("관장"),
    teacher("사범"),
    member("관원");

    private final String label;
}
