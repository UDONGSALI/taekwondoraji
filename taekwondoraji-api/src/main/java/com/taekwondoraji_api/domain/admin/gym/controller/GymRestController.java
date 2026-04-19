package com.taekwondoraji_api.domain.admin.gym.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.admin.gym.dto.GymUpdateRequest;
import com.taekwondoraji_api.domain.admin.gym.service.GymCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/gym")
public class GymRestController {

    private final GymCommandService gymCommandService;

    @PatchMapping("/{gymId}")
    public ApiResponse<Void> updateGym(
            @PathVariable Integer gymId,
            @Valid @RequestBody GymUpdateRequest request
    ) {
        gymCommandService.updateGym(gymId, request);
        return ApiResponse.ok();
    }
}
