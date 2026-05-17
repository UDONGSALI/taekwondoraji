package com.taekwondoraji_api.domain.member.entity;

public enum PointSource {
    attendance,
    goal,
    daily_quest,
    event,
    manual;

    public String getLabel() {
        return switch (this) {
            case attendance -> "출석";
            case goal -> "목표";
            case daily_quest -> "일일 퀘스트";
            case event -> "이벤트";
            case manual -> "수동";
        };
    }
}
