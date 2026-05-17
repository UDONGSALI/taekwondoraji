package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.domain.goal.entity.Goal;
import com.taekwondoraji_api.domain.goal.entity.GoalSource;
import com.taekwondoraji_api.domain.goal.repository.GoalRepository;
import com.taekwondoraji_api.domain.gym.web.dto.GoalInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.GoalInfoRow;
import com.taekwondoraji_api.domain.gym.web.dto.GoalInfoSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymGoalPageService {

    private final GoalRepository goalRepository;

    public GoalInfoPage getGoalInfoPage(Integer gymId) {
        return new GoalInfoPage(
                goalRepository.findVisibleGoalsForGym(gymId, GoalSource.common, GoalSource.custom).stream()
                        .map(goal -> toGoalInfoRow(gymId, goal))
                        .toList(),
                getGoalInfoSummary(gymId)
        );
    }

    private GoalInfoSummary getGoalInfoSummary(Integer gymId) {
        int commonCount = (int) goalRepository.countByGoalSource(GoalSource.common);
        int customCount = (int) goalRepository.countByGoalSourceAndCreatedGymId(GoalSource.custom, gymId);

        return new GoalInfoSummary(
                commonCount + customCount,
                commonCount,
                customCount
        );
    }

    private GoalInfoRow toGoalInfoRow(Integer gymId, Goal goal) {
        boolean deletable = goal.getGoalSource() == GoalSource.custom
                && gymId.equals(goal.getCreatedGymId());

        return new GoalInfoRow(
                goal.getGoalId(),
                goal.getName(),
                goal.getDescription(),
                goal.getLink(),
                goal.getCategory(),
                goal.getPoint(),
                goal.getGoalSource().getLabel(),
                deletable,
                goal.getCreatedAt()
        );
    }
}
