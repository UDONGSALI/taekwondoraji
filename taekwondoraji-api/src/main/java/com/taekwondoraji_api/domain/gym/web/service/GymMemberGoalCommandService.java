package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.goal.entity.Goal;
import com.taekwondoraji_api.domain.goal.entity.GoalStatus;
import com.taekwondoraji_api.domain.goal.entity.MemberGoal;
import com.taekwondoraji_api.domain.goal.repository.MemberGoalRepository;
import com.taekwondoraji_api.domain.gym.web.dto.MemberGoalBulkCompleteRequest;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberGymPointHistory;
import com.taekwondoraji_api.domain.member.repository.MemberGymPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GymMemberGoalCommandService {

    private final MemberGoalRepository memberGoalRepository;
    private final MemberGymPointHistoryRepository memberGymPointHistoryRepository;

    public void completeMemberGoal(Integer gymId, Integer memberGoalId) {
        MemberGoal memberGoal = memberGoalRepository
                .findByIdAndGymIdAndGoalStatus(memberGoalId, gymId, GoalStatus.progress)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        completeAndEarnPoint(memberGoal);
    }

    public void completeMemberGoals(Integer gymId, MemberGoalBulkCompleteRequest request) {
        List<MemberGoal> memberGoals = memberGoalRepository.findAllByIdsAndGymIdAndGoalStatus(
                request.memberGoalIds(),
                gymId,
                GoalStatus.progress
        );

        memberGoals.forEach(this::completeAndEarnPoint);
    }

    private void completeAndEarnPoint(MemberGoal memberGoal) {
        memberGoal.complete();

        MemberGymMap memberGymMap = memberGoal.getMemberGymMap();
        Goal goal = memberGoal.getGoal();
        Integer point = goal.getPoint() == null ? 0 : goal.getPoint();
        memberGymMap.addPoint(point);

        memberGymPointHistoryRepository.save(MemberGymPointHistory.earnGoal(
                memberGymMap,
                point,
                null,
                "목표 완료: " + goal.getName()
        ));
    }
}
