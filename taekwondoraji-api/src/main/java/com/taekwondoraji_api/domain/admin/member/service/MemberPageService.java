package com.taekwondoraji_api.domain.admin.member.service;

import com.taekwondoraji_api.domain.admin.member.dto.MemberInfoPage;
import com.taekwondoraji_api.domain.admin.member.dto.MemberInfoParam;
import com.taekwondoraji_api.domain.admin.member.dto.MemberInfoRow;
import com.taekwondoraji_api.domain.admin.member.dto.MemberInfoSummary;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberRole;
import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
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
public class MemberPageService {

    private final MemberGymMapRepository memberGymMapRepository;

    public MemberInfoPage getMemberInfoPage(MemberInfoParam param) {
        Specification<MemberGymMap> spec = createMemberInfoSpec(param);

        List<MemberInfoRow> list = memberGymMapRepository.findAllBySpec(
                        spec,
                        Sort.by(Sort.Direction.DESC, "memberGymMapId")
                ).stream()
                .map(this::toMemberInfoRow)
                .toList();

        return new MemberInfoPage(list, getMemberInfoSummary());
    }

    public MemberInfoSummary getMemberInfoSummary() {
        return new MemberInfoSummary(
                (int) memberGymMapRepository.count(),
                (int) memberGymMapRepository.countByMemberStatus(MemberStatus.wait),
                (int) memberGymMapRepository.countByMemberStatus(MemberStatus.active),
                (int) memberGymMapRepository.countByMemberStatus(MemberStatus.stop)
        );
    }

    private Specification<MemberGymMap> createMemberInfoSpec(MemberInfoParam param) {
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

        return Specification.where(MemberGymMapSpecification.memberRoleEquals(memberRole))
                .and(MemberGymMapSpecification.memberStatusEquals(memberStatus));
    }

    private MemberInfoRow toMemberInfoRow(MemberGymMap memberGymMap) {
        return new MemberInfoRow(
                memberGymMap.getMemberInfo().getMemberId(),
                memberGymMap.getMemberInfo().getMemberName(),
                memberGymMap.getMemberInfo().getLoginId(),
                memberGymMap.getMemberInfo().getPhoneNumber(),
                memberGymMap.getGym().getGymName(),
                memberGymMap.getMemberRole().getLabel(),
                memberGymMap.getMemberStatus().getLabel(),
                memberGymMap.getCreatedDt()
        );
    }
}
