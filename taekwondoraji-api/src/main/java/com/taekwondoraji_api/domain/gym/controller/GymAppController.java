package com.taekwondoraji_api.domain.gym.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.gym.dto.GymListResponse;
import com.taekwondoraji_api.domain.gym.dto.RegionResponse;
import com.taekwondoraji_api.domain.gym.service.GymAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gyms")
public class GymAppController {

    private final GymAppService gymAppService;

    @GetMapping("/regions")
    public ApiResponse<List<RegionResponse>> getRegions() {
        return ApiResponse.ok(gymAppService.getRegions());
    }

    @GetMapping
    public ApiResponse<List<GymListResponse>> getGyms(
            @RequestParam(required = false) String regionCode
    ) {
        return ApiResponse.ok(gymAppService.getGyms(regionCode));
    }
}
