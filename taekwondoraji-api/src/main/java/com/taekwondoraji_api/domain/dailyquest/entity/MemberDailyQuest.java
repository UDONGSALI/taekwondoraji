package com.taekwondoraji_api.domain.dailyquest.entity;

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
@Table(name = "member_daily_quest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDailyQuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_daily_quest_id")
    private Integer memberDailyQuestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_gym_map_id", nullable = false)
    private MemberGymMap memberGymMap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_daily_quest_id", nullable = false)
    private GymDailyQuest gymDailyQuest;

    @Enumerated(EnumType.STRING)
    @Column(name = "quest_status", nullable = false)
    private DailyQuestStatus questStatus;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public static MemberDailyQuest create(MemberGymMap memberGymMap, GymDailyQuest gymDailyQuest) {
        MemberDailyQuest memberDailyQuest = new MemberDailyQuest();
        memberDailyQuest.memberGymMap = memberGymMap;
        memberDailyQuest.gymDailyQuest = gymDailyQuest;
        memberDailyQuest.questStatus = DailyQuestStatus.progress;
        return memberDailyQuest;
    }

    public void complete() {
        this.questStatus = DailyQuestStatus.complete;
        this.completedAt = LocalDateTime.now();
    }
}
