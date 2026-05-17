package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.common.code.BeltCode;
import com.taekwondoraji_api.domain.gym.web.dto.MemberBeltUpdateRequest;
import com.taekwondoraji_api.domain.gym.web.dto.MemberDetailResponse;
import com.taekwondoraji_api.domain.gym.web.dto.MemberStatusUpdateRequest;
import com.taekwondoraji_api.domain.member.entity.MemberGymBeltHistory;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.repository.MemberGymBeltHistoryRepository;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GymMemberCommandService {

    private final MemberGymMapRepository memberGymMapRepository;
    private final MemberGymBeltHistoryRepository memberGymBeltHistoryRepository;

    public void updateMemberStatus(Integer gymId, Integer memberId, MemberStatusUpdateRequest request) {
        MemberGymMap memberGymMap = memberGymMapRepository.findByGym_GymIdAndMemberInfo_MemberId(gymId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        memberGymMap.updateMemberStatus(request.memberStatus());
    }

    public MemberDetailResponse updateMemberBelt(Integer gymId, Integer memberGymMapId, MemberBeltUpdateRequest request) {
        MemberGymMap memberGymMap = memberGymMapRepository.findByMemberGymMapIdAndGym_GymId(memberGymMapId, gymId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        String nextBeltName = request.beltName().trim();
        if (!BeltCode.exists(nextBeltName)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        String beforeBeltName = memberGymMap.getBeltName();
        if (!nextBeltName.equals(beforeBeltName)) {
            memberGymMap.updateBeltName(nextBeltName);
            memberGymBeltHistoryRepository.save(MemberGymBeltHistory.create(
                    memberGymMap,
                    beforeBeltName,
                    nextBeltName,
                    null,
                    "\uB760 \uBCC0\uACBD"
            ));
        }

        return MemberDetailResponse.from(memberGymMap);
    }
}
