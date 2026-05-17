package com.taekwondoraji_api.domain.member.entity;

public enum PointType {
    earn,
    use,
    cancel;

    public String getLabel() {
        return switch (this) {
            case earn -> "적립";
            case use -> "사용";
            case cancel -> "취소";
        };
    }
}
