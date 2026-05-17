package com.taekwondoraji_api.domain.pointitem.entity;

import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member_point_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPointItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_point_item_id")
    private Integer memberPointItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_gym_map_id", nullable = false)
    private MemberGymMap memberGymMap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_point_item_id", nullable = false)
    private GymPointItem gymPointItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status", nullable = false)
    private MemberPointItemStatus itemStatus;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static MemberPointItem hold(MemberGymMap memberGymMap, GymPointItem gymPointItem) {
        MemberPointItem memberPointItem = new MemberPointItem();
        memberPointItem.memberGymMap = memberGymMap;
        memberPointItem.gymPointItem = gymPointItem;
        memberPointItem.itemStatus = MemberPointItemStatus.hold;
        return memberPointItem;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
