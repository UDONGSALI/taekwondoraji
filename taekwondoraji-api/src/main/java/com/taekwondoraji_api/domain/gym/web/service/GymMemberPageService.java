package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.common.code.BeltCode;
import com.taekwondoraji_api.domain.gym.web.dto.MemberInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.MemberInfoParam;
import com.taekwondoraji_api.domain.gym.web.dto.MemberInfoRow;
import com.taekwondoraji_api.domain.gym.web.dto.MemberInfoSummary;
import com.taekwondoraji_api.domain.gym.web.dto.MemberBeltHistoryResponse;
import com.taekwondoraji_api.domain.gym.web.dto.MemberDetailResponse;
import com.taekwondoraji_api.domain.gym.web.dto.MemberPointHistoryResponse;
import com.taekwondoraji_api.domain.member.repository.MemberGymBeltHistoryRepository;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberRole;
import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import com.taekwondoraji_api.domain.member.repository.MemberGymPointHistoryRepository;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymMemberPageService {

    private final MemberGymMapRepository memberGymMapRepository;
    private final MemberGymBeltHistoryRepository memberGymBeltHistoryRepository;
    private final MemberGymPointHistoryRepository memberGymPointHistoryRepository;

    public MemberInfoPage getMemberInfoPage(Integer gymId, MemberInfoParam param) {
        Specification<MemberGymMap> spec = createMemberInfoSpec(gymId, param);

        List<MemberInfoRow> list = memberGymMapRepository.findAllBySpec(
                        spec,
                        Sort.by(Sort.Direction.DESC, "memberGymMapId")
                ).stream()
                .map(this::toMemberInfoRow)
                .toList();

        return new MemberInfoPage(list, getMemberInfoSummary(gymId));
    }

    public List<MemberBeltHistoryResponse> getBeltHistories(Integer gymId, Integer memberGymMapId) {
        return memberGymBeltHistoryRepository
                .findAllByMemberGymMapIdAndGymId(
                        memberGymMapId,
                        gymId
                )
                .stream()
                .map(MemberBeltHistoryResponse::from)
                .toList();
    }

    public MemberDetailResponse getMemberDetail(Integer gymId, Integer memberGymMapId) {
        MemberGymMap memberGymMap = memberGymMapRepository.findByMemberGymMapIdAndGym_GymId(memberGymMapId, gymId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberDetailResponse.from(memberGymMap);
    }

    public List<MemberPointHistoryResponse> getPointHistories(Integer gymId, Integer memberGymMapId) {
        return memberGymPointHistoryRepository
                .findAllByMemberGymMapIdAndGymId(
                        memberGymMapId,
                        gymId
                )
                .stream()
                .map(MemberPointHistoryResponse::from)
                .toList();
    }

    private MemberInfoSummary getMemberInfoSummary(Integer gymId) {
        return new MemberInfoSummary(
                (int) memberGymMapRepository.countByGym_GymId(gymId),
                (int) memberGymMapRepository.countByGym_GymIdAndMemberStatus(gymId, MemberStatus.wait),
                (int) memberGymMapRepository.countByGym_GymIdAndMemberStatus(gymId, MemberStatus.active),
                (int) memberGymMapRepository.countByGym_GymIdAndMemberStatus(gymId, MemberStatus.stop)
        );
    }

    private Specification<MemberGymMap> createMemberInfoSpec(Integer gymId, MemberInfoParam param) {
        MemberRole memberRole = null;
        MemberStatus memberStatus = null;

        if (param.getMemberRole() != null && !param.getMemberRole().isBlank()) {
            try {
                memberRole = MemberRole.valueOf(param.getMemberRole().trim());
            } catch (IllegalArgumentException exception) {
                memberRole = null;
            }
        }

        if (param.getMemberStatus() != null && !param.getMemberStatus().isBlank()) {
            try {
                memberStatus = MemberStatus.valueOf(param.getMemberStatus().trim());
            } catch (IllegalArgumentException exception) {
                memberStatus = null;
            }
        }

        return Specification.where(MemberGymMapSpecification.gymIdEquals(gymId))
                .and(MemberGymMapSpecification.memberRoleEquals(memberRole))
                .and(MemberGymMapSpecification.memberStatusEquals(memberStatus));
    }

    private MemberInfoRow toMemberInfoRow(MemberGymMap memberGymMap) {
        return new MemberInfoRow(
                memberGymMap.getMemberGymMapId(),
                memberGymMap.getMemberInfo().getMemberId(),
                memberGymMap.getMemberInfo().getMemberName(),
                memberGymMap.getMemberInfo().getPhoneNumber(),
                memberGymMap.getMemberRole().getLabel(),
                memberGymMap.getMemberStatus().name(),
                memberGymMap.getMemberStatus().getLabel(),
                memberGymMap.getBeltName(),
                BeltCode.label(memberGymMap.getBeltName()),
                memberGymMap.getPoint(),
                memberGymMap.getCreatedAt()
        );
    }
}
