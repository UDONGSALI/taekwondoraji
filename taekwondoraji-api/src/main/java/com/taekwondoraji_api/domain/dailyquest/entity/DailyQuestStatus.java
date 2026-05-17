package com.taekwondoraji_api.domain.dailyquest.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DailyQuestStatus {
    progress("진행"),
    complete("완료"),
    cancel("취소");

    private final String label;
}
