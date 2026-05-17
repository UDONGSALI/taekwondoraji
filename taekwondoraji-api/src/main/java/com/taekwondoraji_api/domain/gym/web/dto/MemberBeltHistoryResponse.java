package com.taekwondoraji_api.domain.gym.web.dto;

import com.taekwondoraji_api.common.code.BeltCode;
import com.taekwondoraji_api.domain.member.entity.MemberGymBeltHistory;

import java.time.LocalDateTime;

public record MemberBeltHistoryResponse(
        Integer beltHistoryId,
        String beforeBeltName,
        String beforeBeltLabel,
        String afterBeltName,
        String afterBeltLabel,
        Integer changedByMemberId,
        String reason,
        LocalDateTime createdAt
) {

    public static MemberBeltHistoryResponse from(MemberGymBeltHistory history) {
        return new MemberBeltHistoryResponse(
                history.getBeltHistoryId(),
                history.getBeforeBeltName(),
                BeltCode.label(history.getBeforeBeltName()),
                history.getAfterBeltName(),
                BeltCode.label(history.getAfterBeltName()),
                history.getChangedByMemberId(),
                history.getReason(),
                history.getCreatedAt()
        );
    }
}
