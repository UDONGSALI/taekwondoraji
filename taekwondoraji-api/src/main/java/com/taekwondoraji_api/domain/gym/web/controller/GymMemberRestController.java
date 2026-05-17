package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.common.response.ApiResponse;
import com.taekwondoraji_api.domain.attendance.dto.AttendanceResponse;
import com.taekwondoraji_api.domain.attendance.service.AttendanceService;
import com.taekwondoraji_api.domain.gym.web.dto.AttendanceDateRequest;
import com.taekwondoraji_api.domain.gym.web.dto.MemberBeltHistoryResponse;
import com.taekwondoraji_api.domain.gym.web.dto.MemberBeltUpdateRequest;
import com.taekwondoraji_api.domain.gym.web.dto.MemberDetailResponse;
import com.taekwondoraji_api.domain.gym.web.dto.MemberPointHistoryResponse;
import com.taekwondoraji_api.domain.gym.web.dto.MemberStatusUpdateRequest;
import com.taekwondoraji_api.domain.gym.web.service.GymMemberCommandService;
import com.taekwondoraji_api.domain.gym.web.service.GymMemberPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gym/api/{gymId}/members")
public class GymMemberRestController {

    private final GymMemberCommandService gymMemberCommandService;
    private final GymMemberPageService gymMemberPageService;
    private final AttendanceService attendanceService;

    @PatchMapping("/{memberId}/status")
    public ApiResponse<Void> updateMemberStatus(
            @PathVariable Integer gymId,
            @PathVariable Integer memberId,
            @Valid @RequestBody MemberStatusUpdateRequest request
    ) {
        gymMemberCommandService.updateMemberStatus(gymId, memberId, request);
        return ApiResponse.ok();
    }

    @GetMapping("/{memberGymMapId}")
    public ApiResponse<MemberDetailResponse> getMemberDetail(
            @PathVariable Integer gymId,
            @PathVariable Integer memberGymMapId
    ) {
        return ApiResponse.ok(gymMemberPageService.getMemberDetail(gymId, memberGymMapId));
    }

    @PatchMapping("/{memberGymMapId}/belt")
    public ApiResponse<MemberDetailResponse> updateMemberBelt(
            @PathVariable Integer gymId,
            @PathVariable Integer memberGymMapId,
            @Valid @RequestBody MemberBeltUpdateRequest request
    ) {
        return ApiResponse.ok(gymMemberCommandService.updateMemberBelt(gymId, memberGymMapId, request));
    }

    @GetMapping("/{memberGymMapId}/belt-histories")
    public ApiResponse<List<MemberBeltHistoryResponse>> getBeltHistories(
            @PathVariable Integer gymId,
            @PathVariable Integer memberGymMapId
    ) {
        return ApiResponse.ok(gymMemberPageService.getBeltHistories(gymId, memberGymMapId));
    }

    @GetMapping("/{memberGymMapId}/point-histories")
    public ApiResponse<List<MemberPointHistoryResponse>> getPointHistories(
            @PathVariable Integer gymId,
            @PathVariable Integer memberGymMapId
    ) {
        return ApiResponse.ok(gymMemberPageService.getPointHistories(gymId, memberGymMapId));
    }

    @PostMapping("/{memberGymMapId}/attendances")
    public ApiResponse<AttendanceResponse> attendDate(
            @PathVariable Integer gymId,
            @PathVariable Integer memberGymMapId,
            @Valid @RequestBody AttendanceDateRequest request
    ) {
        return ApiResponse.ok(attendanceService.attendDate(gymId, memberGymMapId, request.attendanceDate()));
    }

    @DeleteMapping("/{memberGymMapId}/attendances")
    public ApiResponse<Void> deleteAttendanceDate(
            @PathVariable Integer gymId,
            @PathVariable Integer memberGymMapId,
            @Valid @RequestBody AttendanceDateRequest request
    ) {
        attendanceService.deleteAttendanceDate(gymId, memberGymMapId, request.attendanceDate());
        return ApiResponse.ok();
    }
}
