package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.goal.entity.Goal;
import com.taekwondoraji_api.domain.goal.entity.GoalSource;
import com.taekwondoraji_api.domain.goal.repository.GoalRepository;
import com.taekwondoraji_api.domain.gym.web.dto.GoalCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GymGoalCommandService {

    private final GoalRepository goalRepository;

    public void createGoal(Integer gymId, GoalCreateForm form) {
        Goal goal = new Goal(
                form.getName(),
                blankToNull(form.getDescription()),
                blankToNull(form.getLink()),
                form.getCategory(),
                form.getPoint(),
                GoalSource.custom,
                gymId
        );

        goalRepository.save(goal);
    }

    public void deleteGoal(Integer gymId, Integer goalId) {
        Goal goal = goalRepository.findByGoalIdAndGoalSourceAndCreatedGymId(goalId, GoalSource.custom, gymId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        goalRepository.delete(goal);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value;
    }
}
