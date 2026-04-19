package com.taekwondoraji_api.domain.goal.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoalSource {
    common("공통"),
    custom("커스텀");

    private final String label;
}
