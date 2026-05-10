package com.taekwondoraji_api.domain.goal.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.goal.dto.GoalCategory;
import com.taekwondoraji_api.domain.goal.dto.GoalCategoryLevelResponse;
import com.taekwondoraji_api.domain.goal.repository.MemberGoalRepository;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalLevelService {

    private final MemberGoalRepository memberGoalRepository;
    private final MemberGymMapRepository memberGymMapRepository;

    public List<GoalCategoryLevelResponse> getCategoryLevels(Integer memberGymMapId) {
        if (!memberGymMapRepository.existsById(memberGymMapId)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Map<String, Integer> pointByCategory = memberGoalRepository.sumCompletedPointByCategory(memberGymMapId)
                .stream()
                .collect(Collectors.toMap(
                        item -> item.category(),
                        item -> item.point() == null ? 0 : item.point().intValue()
                ));

        return List.of(
                toResponse(GoalCategory.BASIC_STRENGTH, pointByCategory),
                toResponse(GoalCategory.POOMSAE, pointByCategory),
                toResponse(GoalCategory.OUTDOOR_ACTIVITY, pointByCategory),
                toResponse(GoalCategory.BREAKING, pointByCategory),
                toResponse(GoalCategory.KICKING, pointByCategory),
                toResponse(GoalCategory.SPARRING, pointByCategory)
        );
    }

    private GoalCategoryLevelResponse toResponse(GoalCategory category, Map<String, Integer> pointByCategory) {
        int point = pointByCategory.getOrDefault(category.getValue(), 0);
        return new GoalCategoryLevelResponse(
                category.getValue(),
                point,
                calculateLevel(point)
        );
    }

    private int calculateLevel(int point) {
        int level = 0;
        int remainingPoint = point;
        int requiredPoint = 1000;

        while (remainingPoint >= requiredPoint) {
            remainingPoint -= requiredPoint;
            level++;
            requiredPoint += 200;
        }

        return level;
    }
}
