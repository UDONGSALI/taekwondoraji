package com.taekwondoraji_api.domain.member.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.gym.entity.GymInfo;
import com.taekwondoraji_api.domain.gym.repository.GymInfoRepository;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberInfo;
import com.taekwondoraji_api.domain.member.dto.MyGymResponse;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import com.taekwondoraji_api.domain.member.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberGymService {

    private final MemberGymMapRepository memberGymMapRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final GymInfoRepository gymInfoRepository;

    public List<MyGymResponse> getMyGyms(Integer memberId) {
        return memberGymMapRepository.findAllByMemberInfo_MemberIdOrderByMemberGymMapIdDesc(memberId)
                .stream()
                .map(MyGymResponse::from)
                .toList();
    }

    @Transactional
    public MyGymResponse joinGym(Integer memberId, Integer gymId) {
        if (memberGymMapRepository.existsByMemberInfo_MemberIdAndGym_GymId(memberId, gymId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_MEMBER_GYM);
        }

        MemberInfo memberInfo = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        GymInfo gym = gymInfoRepository.findById(gymId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));

        MemberGymMap memberGymMap = MemberGymMap.create(memberInfo, gym);
        return MyGymResponse.from(memberGymMapRepository.save(memberGymMap));
    }
}
