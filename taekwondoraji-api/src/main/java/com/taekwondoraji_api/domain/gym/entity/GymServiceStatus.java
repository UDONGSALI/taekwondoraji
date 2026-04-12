package com.taekwondoraji_api.domain.gym.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GymServiceStatus {
    wait("대기"),
    active("운영 중"),
    stop("중지");

    private final String label;
}
