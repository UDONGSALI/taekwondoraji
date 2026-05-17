package com.taekwondoraji_api.domain.pointitem.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.pointitem.dto.PointItemStoreResponse;
import com.taekwondoraji_api.domain.pointitem.service.PointItemAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointItemAppController {

    private final PointItemAppService pointItemAppService;

    @GetMapping("/api/member-gym-maps/{memberGymMapId}/point-items")
    public ApiResponse<PointItemStoreResponse> getStore(@PathVariable Integer memberGymMapId) {
        return ApiResponse.ok(pointItemAppService.getStore(memberGymMapId));
    }

    @PostMapping("/api/member-gym-maps/{memberGymMapId}/point-items/{gymPointItemId}/purchase")
    public ApiResponse<PointItemStoreResponse> purchase(
            @PathVariable Integer memberGymMapId,
            @PathVariable Integer gymPointItemId
    ) {
        return ApiResponse.ok(pointItemAppService.purchase(memberGymMapId, gymPointItemId));
    }
}
