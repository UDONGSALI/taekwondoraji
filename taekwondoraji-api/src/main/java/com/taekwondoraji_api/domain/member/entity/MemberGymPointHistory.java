package com.taekwondoraji_api.domain.member.entity;

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
@Table(name = "member_gym_point_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGymPointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id")
    private Integer pointHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_gym_map_id", nullable = false)
    private MemberGymMap memberGymMap;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_type", nullable = false)
    private PointType pointType;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_source", nullable = false)
    private PointSource pointSource;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Column(name = "changed_by_member_id")
    private Integer changedByMemberId;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static MemberGymPointHistory earnGoal(
            MemberGymMap memberGymMap,
            Integer point,
            Integer changedByMemberId,
            String reason
    ) {
        MemberGymPointHistory history = new MemberGymPointHistory();
        history.memberGymMap = memberGymMap;
        history.pointType = PointType.earn;
        history.pointSource = PointSource.goal;
        history.point = point;
        history.changedByMemberId = changedByMemberId;
        history.reason = reason;
        return history;
    }

    public static MemberGymPointHistory earnDailyQuest(
            MemberGymMap memberGymMap,
            Integer point,
            Integer changedByMemberId,
            String reason
    ) {
        MemberGymPointHistory history = new MemberGymPointHistory();
        history.memberGymMap = memberGymMap;
        history.pointType = PointType.earn;
        history.pointSource = PointSource.daily_quest;
        history.point = point;
        history.changedByMemberId = changedByMemberId;
        history.reason = reason;
        return history;
    }

    public static MemberGymPointHistory usePointItem(
            MemberGymMap memberGymMap,
            Integer point,
            Integer changedByMemberId,
            String reason
    ) {
        MemberGymPointHistory history = new MemberGymPointHistory();
        history.memberGymMap = memberGymMap;
        history.pointType = PointType.use;
        history.pointSource = PointSource.manual;
        history.point = point;
        history.changedByMemberId = changedByMemberId;
        history.reason = reason;
        return history;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
