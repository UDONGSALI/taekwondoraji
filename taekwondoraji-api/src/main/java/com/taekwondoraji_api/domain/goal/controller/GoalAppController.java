package com.taekwondoraji_api.domain.goal.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.goal.dto.GoalCategoryLevelResponse;
import com.taekwondoraji_api.domain.goal.dto.GoalListResponse;
import com.taekwondoraji_api.domain.goal.dto.MemberGoalListResponse;
import com.taekwondoraji_api.domain.goal.service.GoalAppService;
import com.taekwondoraji_api.domain.goal.service.GoalLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GoalAppController {

    private final GoalLevelService goalLevelService;
    private final GoalAppService goalAppService;

    @GetMapping("/api/member-gym-maps/{memberGymMapId}/goal-levels")
    public ApiResponse<List<GoalCategoryLevelResponse>> getGoalLevels(@PathVariable Integer memberGymMapId) {
        return ApiResponse.ok(goalLevelService.getCategoryLevels(memberGymMapId));
    }

    @GetMapping("/api/goals")
    public ApiResponse<List<GoalListResponse>> getGoals(@RequestParam(required = false) String category) {
        return ApiResponse.ok(goalAppService.getGoals(category));
    }

    @GetMapping("/api/member-gym-maps/{memberGymMapId}/goals")
    public ApiResponse<List<MemberGoalListResponse>> getMemberGoals(@PathVariable Integer memberGymMapId) {
        return ApiResponse.ok(goalAppService.getMemberGoals(memberGymMapId));
    }

    @PostMapping("/api/member-gym-maps/{memberGymMapId}/goals/{goalId}")
    public ApiResponse<MemberGoalListResponse> applyGoal(
            @PathVariable Integer memberGymMapId,
            @PathVariable Integer goalId
    ) {
        return ApiResponse.ok(goalAppService.applyGoal(memberGymMapId, goalId));
    }

    @DeleteMapping("/api/member-gym-maps/{memberGymMapId}/member-goals/{memberGoalId}")
    public ApiResponse<Void> deleteGoalApplication(
            @PathVariable Integer memberGymMapId,
            @PathVariable Integer memberGoalId
    ) {
        goalAppService.deleteGoalApplication(memberGymMapId, memberGoalId);
        return ApiResponse.ok();
    }
}
