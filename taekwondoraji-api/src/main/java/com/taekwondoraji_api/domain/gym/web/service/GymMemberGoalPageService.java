package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.domain.goal.entity.GoalStatus;
import com.taekwondoraji_api.domain.goal.entity.MemberGoal;
import com.taekwondoraji_api.domain.goal.repository.MemberGoalRepository;
import com.taekwondoraji_api.domain.gym.web.dto.MemberGoalInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.MemberGoalInfoParam;
import com.taekwondoraji_api.domain.gym.web.dto.MemberGoalInfoRow;
import com.taekwondoraji_api.domain.gym.web.dto.MemberGoalInfoSummary;
import com.taekwondoraji_api.domain.gym.web.dto.MemberOption;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymMemberGoalPageService {

    private final MemberGoalRepository memberGoalRepository;
    private final MemberGymMapRepository memberGymMapRepository;

    public MemberGoalInfoPage getMemberGoalInfoPage(Integer gymId, MemberGoalInfoParam param) {
        GoalStatus goalStatus = parseGoalStatus(param.getGoalStatus());

        return new MemberGoalInfoPage(
                memberGoalRepository.findAllByGymIdOrderByMemberGoalIdDesc(
                                gymId,
                                goalStatus,
                                param.getMemberId(),
                                blankToNull(param.getCategory())
                        ).stream()
                        .map(this::toMemberGoalInfoRow)
                        .toList(),
                getMemberGoalInfoSummary(gymId)
        );
    }

    public List<MemberOption> getMemberOptions(Integer gymId) {
        return memberGymMapRepository.findAllByGym_GymIdOrderByMemberGymMapIdDesc(gymId).stream()
                .map(memberGymMap -> new MemberOption(
                        memberGymMap.getMemberInfo().getMemberId(),
                        memberGymMap.getMemberInfo().getMemberName()
                ))
                .toList();
    }

    private MemberGoalInfoSummary getMemberGoalInfoSummary(Integer gymId) {
        return new MemberGoalInfoSummary(
                (int) memberGoalRepository.countByGymId(gymId),
                (int) memberGoalRepository.countByGymIdAndGoalStatus(gymId, GoalStatus.progress),
                (int) memberGoalRepository.countByGymIdAndGoalStatus(gymId, GoalStatus.complete),
                (int) memberGoalRepository.countByGymIdAndGoalStatus(gymId, GoalStatus.cancel)
        );
    }

    private MemberGoalInfoRow toMemberGoalInfoRow(MemberGoal memberGoal) {
        GoalStatus goalStatus = memberGoal.getGoalStatus();

        return new MemberGoalInfoRow(
                memberGoal.getMemberGoalId(),
                memberGoal.getMemberGymMap().getMemberInfo().getMemberId(),
                memberGoal.getMemberGymMap().getMemberInfo().getMemberName(),
                memberGoal.getMemberGymMap().getMemberRole().getLabel(),
                memberGoal.getGoal().getGoalId(),
                memberGoal.getGoal().getName(),
                memberGoal.getGoal().getCategory(),
                memberGoal.getGoal().getPoint(),
                goalStatus.name(),
                goalStatus.getLabel(),
                memberGoal.getCompletedAt(),
                goalStatus == GoalStatus.progress
        );
    }

    private GoalStatus parseGoalStatus(String goalStatus) {
        if (goalStatus == null || goalStatus.isBlank()) {
            return null;
        }

        try {
            return GoalStatus.valueOf(goalStatus.trim());
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
