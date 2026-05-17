package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.gym.web.dto.MemberGoalBulkCompleteRequest;
import com.taekwondoraji_api.domain.gym.web.service.GymMemberGoalCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gym/api/{gymId}/member-goals")
public class GymMemberGoalRestController {

    private final GymMemberGoalCommandService gymMemberGoalCommandService;

    @PatchMapping("/{memberGoalId}/complete")
    public ApiResponse<Void> completeMemberGoal(
            @PathVariable Integer gymId,
            @PathVariable Integer memberGoalId
    ) {
        gymMemberGoalCommandService.completeMemberGoal(gymId, memberGoalId);
        return ApiResponse.ok();
    }

    @PatchMapping("/complete-bulk")
    public ApiResponse<Void> completeMemberGoals(
            @PathVariable Integer gymId,
            @Valid @RequestBody MemberGoalBulkCompleteRequest request
    ) {
        gymMemberGoalCommandService.completeMemberGoals(gymId, request);
        return ApiResponse.ok();
    }
}
