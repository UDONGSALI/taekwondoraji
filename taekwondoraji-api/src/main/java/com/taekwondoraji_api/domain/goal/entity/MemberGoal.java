package com.taekwondoraji_api.domain.goal.entity;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member_goal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_goal_id")
    private Integer memberGoalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_gym_map_id", nullable = false)
    private MemberGymMap memberGymMap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_status", nullable = false)
    private GoalStatus goalStatus;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public static MemberGoal create(MemberGymMap memberGymMap, Goal goal) {
        MemberGoal memberGoal = new MemberGoal();
        memberGoal.memberGymMap = memberGymMap;
        memberGoal.goal = goal;
        memberGoal.goalStatus = GoalStatus.progress;
        return memberGoal;
    }
}
