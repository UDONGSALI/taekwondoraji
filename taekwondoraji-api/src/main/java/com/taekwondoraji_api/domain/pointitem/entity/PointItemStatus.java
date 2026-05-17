package com.taekwondoraji_api.domain.pointitem.entity;

public enum PointItemStatus {
    active("판매"),
    stop("중지");

    private final String label;

    PointItemStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
