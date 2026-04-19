package com.taekwondoraji_api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "region_code_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegionCodeInfo {

    @Id
    @Column(name = "region_code", nullable = false, length = 4)
    private String regionCode;

    @Column(name = "region_name", nullable = false, length = 20)
    private String regionName;
}
