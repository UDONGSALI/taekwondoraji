package com.taekwondoraji_api.domain.gym.web.dto;

import com.taekwondoraji_api.domain.member.entity.MemberGymPointHistory;

import java.time.LocalDateTime;

public record MemberPointHistoryResponse(
        Integer pointHistoryId,
        String pointType,
        String pointTypeLabel,
        String pointSource,
        String pointSourceLabel,
        Integer point,
        Integer changedByMemberId,
        String reason,
        LocalDateTime createdAt
) {

    public static MemberPointHistoryResponse from(MemberGymPointHistory history) {
        return new MemberPointHistoryResponse(
                history.getPointHistoryId(),
                history.getPointType().name(),
                history.getPointType().getLabel(),
                history.getPointSource().name(),
                history.getPointSource().getLabel(),
                history.getPoint(),
                history.getChangedByMemberId(),
                history.getReason(),
                history.getCreatedAt()
        );
    }
}
