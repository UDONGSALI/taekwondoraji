package com.taekwondoraji_api.domain.gym.entity;

import com.taekwondoraji_api.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "gym_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GymInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_id")
    private Integer gymId;

    @Column(name = "gym_name", nullable = false, length = 100)
    private String gymName;

    @Column(name = "business_number", nullable = false, unique = true, length = 10)
    private String businessNumber;

    @Column(name = "owner_name", nullable = false, length = 50)
    private String ownerName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "address_road", length = 255)
    private String addressRoad;

    @Column(name = "address_detail", length = 255)
    private String addressDetail;

    @Column(name = "region_code", length = 20)
    private String regionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_status", nullable = false)
    private GymServiceStatus serviceStatus;

    @Column(name = "service_start_date")
    private LocalDate serviceStartDate;

    @Column(name = "service_end_date")
    private LocalDate serviceEndDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
