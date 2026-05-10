package com.taekwondoraji_api.domain.goal.dto;

import com.taekwondoraji_api.domain.goal.entity.Goal;
import com.taekwondoraji_api.domain.goal.entity.MemberGoal;

import java.time.LocalDateTime;

public record MemberGoalListResponse(
        Integer goalId,
        Integer memberGoalId,
        String name,
        String description,
        String link,
        String category,
        Integer point,
        String goalStatus,
        LocalDateTime completedAt,
        boolean reapplyAvailable
) {

    public static MemberGoalListResponse waiting(Goal goal) {
        return waiting(goal, false);
    }

    public static MemberGoalListResponse waiting(Goal goal, boolean reapplyAvailable) {
        return from(goal, null, "waiting", reapplyAvailable);
    }

    public static MemberGoalListResponse from(MemberGoal memberGoal) {
        return from(memberGoal.getGoal(), memberGoal, memberGoal.getGoalStatus().name(), false);
    }

    private static MemberGoalListResponse from(
            Goal goal,
            MemberGoal memberGoal,
            String goalStatus,
            boolean reapplyAvailable
    ) {
        return new MemberGoalListResponse(
                goal.getGoalId(),
                memberGoal == null ? null : memberGoal.getMemberGoalId(),
                goal.getName(),
                goal.getDescription(),
                goal.getLink(),
                goal.getCategory(),
                goal.getPoint(),
                goalStatus,
                memberGoal == null ? null : memberGoal.getCompletedAt(),
                reapplyAvailable
        );
    }
}
