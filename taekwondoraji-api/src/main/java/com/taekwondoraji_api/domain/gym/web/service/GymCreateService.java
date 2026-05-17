package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.gym.entity.GymInfo;
import com.taekwondoraji_api.domain.gym.repository.GymInfoRepository;
import com.taekwondoraji_api.domain.gym.web.dto.GymCreateForm;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberInfo;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import com.taekwondoraji_api.domain.member.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GymCreateService {

    private final GymInfoRepository gymInfoRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MemberGymMapRepository memberGymMapRepository;

    public Integer createGym(Integer memberId, GymCreateForm form) {
        if (memberId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (gymInfoRepository.existsByBusinessNumber(form.getBusinessNumber())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        MemberInfo memberInfo = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        GymInfo gymInfo = GymInfo.create(
                form.getGymName(),
                form.getBusinessNumber(),
                form.getOwnerName(),
                form.getPhoneNumber(),
                form.getPostalCode(),
                form.getAddressRoad(),
                form.getAddressDetail(),
                form.getRegionCode()
        );
        GymInfo savedGym = gymInfoRepository.save(gymInfo);

        memberGymMapRepository.save(MemberGymMap.createManager(memberInfo, savedGym));
        return savedGym.getGymId();
    }
}
