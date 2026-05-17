package com.taekwondoraji_api.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member_gym_belt_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGymBeltHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "belt_history_id")
    private Integer beltHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_gym_map_id", nullable = false)
    private MemberGymMap memberGymMap;

    @Column(name = "before_belt_name", length = 20)
    private String beforeBeltName;

    @Column(name = "after_belt_name", nullable = false, length = 20)
    private String afterBeltName;

    @Column(name = "changed_by_member_id")
    private Integer changedByMemberId;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public static MemberGymBeltHistory create(
            MemberGymMap memberGymMap,
            String beforeBeltName,
            String afterBeltName,
            Integer changedByMemberId,
            String reason
    ) {
        MemberGymBeltHistory history = new MemberGymBeltHistory();
        history.memberGymMap = memberGymMap;
        history.beforeBeltName = beforeBeltName;
        history.afterBeltName = afterBeltName;
        history.changedByMemberId = changedByMemberId;
        history.reason = reason;
        return history;
    }
}
