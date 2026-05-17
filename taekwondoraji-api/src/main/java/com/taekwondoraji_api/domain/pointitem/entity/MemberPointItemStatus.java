package com.taekwondoraji_api.domain.pointitem.entity;

public enum MemberPointItemStatus {
    hold("보유"),
    used("사용"),
    cancel("취소");

    private final String label;

    MemberPointItemStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
