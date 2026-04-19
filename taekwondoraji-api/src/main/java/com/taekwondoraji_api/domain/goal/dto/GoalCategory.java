package com.taekwondoraji_api.domain.goal.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoalCategory {
    BASIC_STRENGTH("기초체력"),
    POOMSAE("품새"),
    OUTDOOR_ACTIVITY("외부활동"),
    BREAKING("격파"),
    KICKING("발차기"),
    SPARRING("겨루기");

    private final String value;
}
