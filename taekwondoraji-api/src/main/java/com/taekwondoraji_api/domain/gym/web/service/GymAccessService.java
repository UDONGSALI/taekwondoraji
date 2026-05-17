package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.domain.gym.web.dto.ManagedGymOption;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberRole;
import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymAccessService {

    private static final Set<MemberRole> MANAGER_ROLES = Set.of(MemberRole.master, MemberRole.teacher);

    private final MemberGymMapRepository memberGymMapRepository;

    public List<ManagedGymOption> findManagedGyms(Integer memberId) {
        if (memberId == null) {
            return List.of();
        }

        return memberGymMapRepository.findAllByMemberInfo_MemberIdOrderByMemberGymMapIdDesc(memberId).stream()
                .filter(this::isManagedGym)
                .map(memberGymMap -> new ManagedGymOption(
                        memberGymMap.getGym().getGymId(),
                        memberGymMap.getGym().getGymName(),
                        memberGymMap.getMemberRole().getLabel(),
                        memberGymMap.getMemberStatus().getLabel()
                ))
                .toList();
    }

    public boolean canManageGym(Integer memberId, Integer gymId) {
        if (memberId == null || gymId == null) {
            return false;
        }

        return memberGymMapRepository.findByGym_GymIdAndMemberInfo_MemberId(gymId, memberId)
                .filter(this::isManagedGym)
                .isPresent();
    }

    private boolean isManagedGym(MemberGymMap memberGymMap) {
        return MANAGER_ROLES.contains(memberGymMap.getMemberRole())
                && memberGymMap.getMemberStatus() == MemberStatus.active;
    }
}
