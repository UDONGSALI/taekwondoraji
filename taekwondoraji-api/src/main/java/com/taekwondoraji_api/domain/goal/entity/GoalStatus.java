package com.taekwondoraji_api.domain.goal.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoalStatus {
    progress("신청"),
    complete("완료"),
    cancel("취소");

    private final String label;
}
