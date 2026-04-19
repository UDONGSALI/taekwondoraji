package com.taekwondoraji_api.domain.admin.gym.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.admin.gym.dto.GymUpdateRequest;
import com.taekwondoraji_api.domain.gym.entity.GymInfo;
import com.taekwondoraji_api.domain.gym.repository.GymInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GymCommandService {

    private final GymInfoRepository gymInfoRepository;

    public void updateGym(Integer gymId, GymUpdateRequest request) {
        if (request.serviceStartDate() != null
                && request.serviceEndDate() != null
                && request.serviceEndDate().isBefore(request.serviceStartDate())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        GymInfo gymInfo = gymInfoRepository.findById(gymId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));

        gymInfo.updateServiceInfo(
                request.serviceStatus(),
                request.serviceStartDate(),
                request.serviceEndDate()
        );
    }
}
