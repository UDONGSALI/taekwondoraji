package com.taekwondoraji_api.domain.dailyquest.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.dailyquest.dto.DailyQuestListResponse;
import com.taekwondoraji_api.domain.dailyquest.service.DailyQuestAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DailyQuestAppController {

    private final DailyQuestAppService dailyQuestAppService;

    @GetMapping("/api/member-gym-maps/{memberGymMapId}/daily-quests")
    public ApiResponse<List<DailyQuestListResponse>> getDailyQuests(@PathVariable Integer memberGymMapId) {
        return ApiResponse.ok(dailyQuestAppService.getDailyQuests(memberGymMapId));
    }

    @PostMapping("/api/member-gym-maps/{memberGymMapId}/daily-quests/{dailyQuestId}")
    public ApiResponse<DailyQuestListResponse> applyDailyQuest(
            @PathVariable Integer memberGymMapId,
            @PathVariable Integer dailyQuestId
    ) {
        return ApiResponse.ok(dailyQuestAppService.applyDailyQuest(memberGymMapId, dailyQuestId));
    }

    @DeleteMapping("/api/member-gym-maps/{memberGymMapId}/member-daily-quests/{memberDailyQuestId}")
    public ApiResponse<Void> deleteDailyQuestApplication(
            @PathVariable Integer memberGymMapId,
            @PathVariable Integer memberDailyQuestId
    ) {
        dailyQuestAppService.deleteDailyQuestApplication(memberGymMapId, memberDailyQuestId);
        return ApiResponse.ok();
    }
}
