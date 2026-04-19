package com.taekwondoraji_api.domain.admin.goal.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.admin.goal.dto.GoalCreateForm;
import com.taekwondoraji_api.domain.goal.entity.Goal;
import com.taekwondoraji_api.domain.goal.entity.GoalSource;
import com.taekwondoraji_api.domain.goal.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalCommandService {

    private final GoalRepository goalRepository;

    public void createGoal(GoalCreateForm form) {
        Goal goal = new Goal(
                form.getName(),
                blankToNull(form.getDescription()),
                blankToNull(form.getLink()),
                form.getCategory(),
                form.getPoint(),
                GoalSource.common,
                null
        );

        goalRepository.save(goal);
    }

    public void deleteGoal(Integer goalId) {
        if (!goalRepository.existsById(goalId)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        goalRepository.deleteById(goalId);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value;
    }
}
