package com.taekwondoraji_api.domain.admin.service;

import com.taekwondoraji_api.domain.admin.dto.AdminDashboardResponse;
import com.taekwondoraji_api.domain.admin.dto.AdminGymInfoResponse;
import com.taekwondoraji_api.domain.admin.dto.GymInfoItem;
import com.taekwondoraji_api.domain.admin.dto.GymInfoSummary;
import com.taekwondoraji_api.domain.gym.entity.GymInfo;
import com.taekwondoraji_api.domain.gym.entity.GymServiceStatus;
import com.taekwondoraji_api.domain.gym.entity.RegionCodeInfo;
import com.taekwondoraji_api.domain.gym.repository.GymInfoRepository;
import com.taekwondoraji_api.domain.gym.repository.RegionCodeInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final GymInfoRepository gymInfoRepository;
    private final RegionCodeInfoRepository regionCodeInfoRepository;

    public AdminDashboardResponse getDashboard() {
        return new AdminDashboardResponse(
                "서비스 운영 관리자",
                "도장 고객, 전체 회원, 구독 상품, 공지와 운영 정책을 통합 관리하는 내부 운영 화면입니다."
        );
    }

    public AdminGymInfoResponse getGymInfoList() {
        List<GymInfo> gymInfos = gymInfoRepository.findAll(
                Sort.by(Sort.Direction.DESC, "gymId")
        );

        Map<String, String> regionNameMap = getRegionNameMap(gymInfos);

        List<GymInfoItem> list = gymInfos.stream()
                .map(gymInfo -> toGymInfoItem(gymInfo, regionNameMap))
                .toList();

        GymInfoSummary summary = new GymInfoSummary(
                gymInfos.size(),
                countByStatus(gymInfos, GymServiceStatus.wait),
                countByStatus(gymInfos, GymServiceStatus.active),
                countByStatus(gymInfos, GymServiceStatus.stop)
        );

        return new AdminGymInfoResponse(list, summary);
    }

    private GymInfoItem toGymInfoItem(GymInfo gymInfo, Map<String, String> regionNameMap) {
        return new GymInfoItem(
                gymInfo.getGymId().longValue(),
                gymInfo.getGymName(),
                gymInfo.getServiceStatus().getLabel(),
                gymInfo.getBusinessNumber(),
                gymInfo.getOwnerName(),
                gymInfo.getPhoneNumber(),
                buildAddress(gymInfo),
                getRegionName(gymInfo.getRegionCode(), regionNameMap),
                gymInfo.getServiceStartDate(),
                gymInfo.getServiceEndDate(),
                gymInfo.getCreatedDt()

        );
    }

    private Map<String, String> getRegionNameMap(List<GymInfo> gymInfos) {
        List<String> regionCodes = gymInfos.stream()
                .map(GymInfo::getRegionCode)
                .filter(Objects::nonNull)
                .filter(regionCode -> !regionCode.isBlank())
                .distinct()
                .toList();

        return regionCodeInfoRepository.findAllById(regionCodes).stream()
                .collect(Collectors.toMap(
                        RegionCodeInfo::getRegionCode,
                        RegionCodeInfo::getRegionName,
                        (existing, replacement) -> existing
                ));
    }

    private String getRegionName(String regionCode, Map<String, String> regionNameMap) {
        if (regionCode == null || regionCode.isBlank()) {
            return "-";
        }

        return regionNameMap.getOrDefault(regionCode, regionCode);
    }

    private String buildAddress(GymInfo gymInfo) {
        String addressRoad = gymInfo.getAddressRoad();
        String addressDetail = gymInfo.getAddressDetail();

        if (addressRoad == null || addressRoad.isBlank()) {
            return "-";
        }

        if (addressDetail == null || addressDetail.isBlank()) {
            return addressRoad;
        }

        return addressRoad + " (" + addressDetail + ")";
    }

    private int countByStatus(List<GymInfo> gymInfos, GymServiceStatus serviceStatus) {
        return (int) gymInfos.stream()
                .filter(gymInfo -> gymInfo.getServiceStatus() == serviceStatus)
                .count();
    }
}
