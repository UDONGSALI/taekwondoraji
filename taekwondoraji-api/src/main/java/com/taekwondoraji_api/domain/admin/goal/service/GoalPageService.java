package com.taekwondoraji_api.domain.admin.goal.service;

import com.taekwondoraji_api.domain.admin.goal.dto.GoalInfoPage;
import com.taekwondoraji_api.domain.admin.goal.dto.GoalInfoRow;
import com.taekwondoraji_api.domain.admin.goal.dto.GoalInfoSummary;
import com.taekwondoraji_api.domain.goal.entity.Goal;
import com.taekwondoraji_api.domain.goal.entity.GoalSource;
import com.taekwondoraji_api.domain.goal.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalPageService {

    private final GoalRepository goalRepository;

    public GoalInfoPage getGoalInfoPage() {
        return new GoalInfoPage(
                goalRepository.findAll(Sort.by(Sort.Direction.DESC, "goalId")).stream()
                        .map(this::toGoalInfoRow)
                        .toList(),
                getGoalInfoSummary()
        );
    }

    public GoalInfoSummary getGoalInfoSummary() {
        return new GoalInfoSummary(
                (int) goalRepository.count(),
                (int) goalRepository.countByGoalSource(GoalSource.common),
                (int) goalRepository.countByGoalSource(GoalSource.custom)
        );
    }

    private GoalInfoRow toGoalInfoRow(Goal goal) {
        return new GoalInfoRow(
                goal.getGoalId(),
                goal.getName(),
                goal.getDescription(),
                goal.getLink(),
                goal.getCategory(),
                goal.getPoint(),
                goal.getGoalSource().getLabel(),
                goal.getCreatedAt()
        );
    }
}
