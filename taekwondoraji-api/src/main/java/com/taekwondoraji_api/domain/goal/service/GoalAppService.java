package com.taekwondoraji_api.domain.goal.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.goal.dto.GoalListResponse;
import com.taekwondoraji_api.domain.goal.dto.MemberGoalListResponse;
import com.taekwondoraji_api.domain.goal.entity.Goal;
import com.taekwondoraji_api.domain.goal.entity.GoalStatus;
import com.taekwondoraji_api.domain.goal.entity.MemberGoal;
import com.taekwondoraji_api.domain.goal.repository.GoalRepository;
import com.taekwondoraji_api.domain.goal.repository.MemberGoalRepository;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalAppService {

    private static final long REAPPLY_WAIT_MONTHS = 3;

    private final GoalRepository goalRepository;
    private final MemberGoalRepository memberGoalRepository;
    private final MemberGymMapRepository memberGymMapRepository;

    public List<GoalListResponse> getGoals(String category) {
        if (category == null || category.isBlank()) {
            return goalRepository.findAllByOrderByPointAscGoalIdDesc()
                    .stream()
                    .map(GoalListResponse::from)
                    .toList();
        }

        return goalRepository.findAllByCategoryOrderByPointAscGoalIdDesc(category)
                .stream()
                .map(GoalListResponse::from)
                .toList();
    }

    public List<MemberGoalListResponse> getMemberGoals(Integer memberGymMapId) {
        if (!memberGymMapRepository.existsById(memberGymMapId)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Map<Integer, MemberGoal> memberGoalByGoalId = memberGoalRepository
                .findAllByMemberGymMap_MemberGymMapIdOrderByMemberGoalIdDesc(memberGymMapId)
                .stream()
                .filter(memberGoal -> memberGoal.getGoalStatus() != GoalStatus.cancel)
                .collect(Collectors.toMap(
                        memberGoal -> memberGoal.getGoal().getGoalId(),
                        Function.identity(),
                        (latest, ignored) -> latest
                ));

        return goalRepository.findAllByOrderByPointAscGoalIdDesc()
                .stream()
                .map(goal -> {
                    MemberGoal memberGoal = memberGoalByGoalId.get(goal.getGoalId());
                    if (memberGoal == null) {
                        return MemberGoalListResponse.waiting(goal);
                    }

                    if (canApplyAgain(memberGoal)) {
                        return MemberGoalListResponse.waiting(goal, true);
                    }

                    return MemberGoalListResponse.from(memberGoal);
                })
                .toList();
    }

    @Transactional
    public MemberGoalListResponse applyGoal(Integer memberGymMapId, Integer goalId) {
        return memberGoalRepository
                .findFirstByMemberGymMap_MemberGymMapIdAndGoal_GoalIdAndGoalStatusInOrderByMemberGoalIdDesc(
                        memberGymMapId,
                        goalId,
                        List.of(GoalStatus.progress, GoalStatus.complete)
                )
                .map(memberGoal -> canApplyAgain(memberGoal)
                        ? createMemberGoal(memberGymMapId, goalId)
                        : MemberGoalListResponse.from(memberGoal))
                .orElseGet(() -> createMemberGoal(memberGymMapId, goalId));
    }

    private boolean canApplyAgain(MemberGoal memberGoal) {
        if (memberGoal.getGoalStatus() != GoalStatus.complete) {
            return false;
        }

        LocalDateTime completedAt = memberGoal.getCompletedAt();
        return completedAt != null
                && !completedAt.plusMonths(REAPPLY_WAIT_MONTHS).isAfter(LocalDateTime.now());
    }

    private MemberGoalListResponse createMemberGoal(Integer memberGymMapId, Integer goalId) {
        MemberGymMap memberGymMap = memberGymMapRepository.findById(memberGymMapId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
        MemberGoal memberGoal = MemberGoal.create(memberGymMap, goal);
        return MemberGoalListResponse.from(memberGoalRepository.save(memberGoal));
    }
}
