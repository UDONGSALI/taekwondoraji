package com.taekwondoraji_api.domain.gym.service;

import com.taekwondoraji_api.common.repository.RegionCodeInfoRepository;
import com.taekwondoraji_api.domain.gym.dto.GymListResponse;
import com.taekwondoraji_api.domain.gym.dto.RegionResponse;
import com.taekwondoraji_api.domain.gym.repository.GymInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymAppService {

    private final GymInfoRepository gymInfoRepository;
    private final RegionCodeInfoRepository regionCodeInfoRepository;

    public List<RegionResponse> getRegions() {
        return regionCodeInfoRepository.findAll(Sort.by(Sort.Direction.ASC, "regionCode"))
                .stream()
                .map(RegionResponse::from)
                .toList();
    }

    public List<GymListResponse> getGyms(String regionCode) {
        if (regionCode == null || regionCode.isBlank()) {
            return gymInfoRepository.findAllByIsActiveTrueOrderByGymNameAsc()
                    .stream()
                    .map(GymListResponse::from)
                    .toList();
        }

        return gymInfoRepository.findAllByIsActiveTrueAndRegionCodeOrderByGymNameAsc(regionCode)
                .stream()
                .map(GymListResponse::from)
                .toList();
    }
}
