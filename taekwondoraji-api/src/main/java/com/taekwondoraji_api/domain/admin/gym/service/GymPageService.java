package com.taekwondoraji_api.domain.admin.gym.service;

import com.taekwondoraji_api.common.dto.RegionOption;
import com.taekwondoraji_api.common.repository.RegionCodeInfoRepository;
import com.taekwondoraji_api.domain.admin.gym.dto.GymDetailPage;
import com.taekwondoraji_api.domain.admin.gym.dto.GymInfoPage;
import com.taekwondoraji_api.domain.admin.gym.dto.GymInfoParam;
import com.taekwondoraji_api.domain.admin.gym.dto.GymInfoRow;
import com.taekwondoraji_api.domain.admin.gym.dto.GymInfoSummary;
import com.taekwondoraji_api.domain.admin.gym.dto.GymMemberRow;
import com.taekwondoraji_api.domain.admin.gym.dto.GymMemberSection;
import com.taekwondoraji_api.domain.gym.entity.GymInfo;
import com.taekwondoraji_api.domain.gym.entity.GymServiceStatus;
import com.taekwondoraji_api.domain.gym.repository.GymInfoRepository;
import com.taekwondoraji_api.domain.gym.repository.GymInfoSpecification;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberRole;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymPageService {

    private final GymInfoRepository gymInfoRepository;
    private final RegionCodeInfoRepository regionCodeInfoRepository;
    private final MemberGymMapRepository memberGymMapRepository;

    public List<RegionOption> getRegionOptions() {
        return regionCodeInfoRepository.findAll(Sort.by(Sort.Direction.ASC, "regionName")).stream()
                .map(region -> new RegionOption(region.getRegionCode(), region.getRegionName()))
                .toList();
    }

    public GymInfoPage getGymInfoPage(GymInfoParam param) {
        Specification<GymInfo> spec = createGymInfoSpec(param);

        List<GymInfo> gymInfoList = gymInfoRepository.findAllBySpec(
                spec,
                Sort.by(Sort.Direction.DESC, "gymId")
        );

        Map<String, String> regionNameMap = getRegionNameMap(gymInfoList);

        List<GymInfoRow> list = gymInfoList.stream()
                .map(gymInfo -> toGymInfoRow(gymInfo, regionNameMap))
                .toList();

        return new GymInfoPage(list, getGymInfoSummary());
    }

    public GymDetailPage getGymDetailPage(Integer gymId) {
        GymInfo gymInfo = gymInfoRepository.findById(gymId)
                .orElseThrow(() -> new IllegalArgumentException("도장 정보가 없습니다."));

        Map<String, String> regionNameMap = getRegionNameMap(List.of(gymInfo));
        GymInfoRow gymInfoRow = toGymInfoRow(gymInfo, regionNameMap);
        GymMemberSection members = getGymMemberSection(gymId);

        return new GymDetailPage(gymInfoRow, members);
    }

    public GymInfoSummary getGymInfoSummary() {
        return new GymInfoSummary(
                (int) gymInfoRepository.count(),
                (int) gymInfoRepository.countByServiceStatus(GymServiceStatus.wait),
                (int) gymInfoRepository.countByServiceStatus(GymServiceStatus.active),
                (int) gymInfoRepository.countByServiceStatus(GymServiceStatus.stop)
        );
    }

    private GymMemberSection getGymMemberSection(Integer gymId) {
        List<MemberGymMap> memberMaps = memberGymMapRepository.findAllByGym_GymIdOrderByMemberGymMapIdDesc(gymId);

        List<GymMemberRow> adminList = memberMaps.stream()
                .filter(memberMap -> memberMap.getMemberRole() == MemberRole.master
                        || memberMap.getMemberRole() == MemberRole.teacher)
                .map(this::toGymMemberRow)
                .toList();

        List<GymMemberRow> memberList = memberMaps.stream()
                .filter(memberMap -> memberMap.getMemberRole() == MemberRole.member)
                .map(this::toGymMemberRow)
                .toList();

        return new GymMemberSection(
                adminList.size(),
                memberList.size(),
                adminList,
                memberList
        );
    }

    private GymMemberRow toGymMemberRow(MemberGymMap memberMap) {
        return new GymMemberRow(
                memberMap.getMemberInfo().getMemberId(),
                memberMap.getMemberInfo().getMemberName(),
                memberMap.getMemberRole().getLabel(),
                memberMap.getMemberStatus().getLabel(),
                memberMap.getMemberInfo().getPhoneNumber()
        );
    }

    private Specification<GymInfo> createGymInfoSpec(GymInfoParam param) {
        GymServiceStatus serviceStatus = null;

        if (param.getServiceStatus() != null && !param.getServiceStatus().isBlank()) {
            try {
                serviceStatus = GymServiceStatus.valueOf(param.getServiceStatus().trim());
            } catch (IllegalArgumentException exception) {
                serviceStatus = null;
            }
        }

        return Specification.where(GymInfoSpecification.keywordContains(param.getKeyword()))
                .and(GymInfoSpecification.regionCodeEquals(param.getRegionCode()))
                .and(GymInfoSpecification.serviceStatusEquals(serviceStatus));
    }

    private GymInfoRow toGymInfoRow(GymInfo gymInfo, Map<String, String> regionNameMap) {
        String regionCode = gymInfo.getRegionCode();
        String regionName = "-";

        if (regionCode != null && !regionCode.isBlank()) {
            regionName = regionNameMap.getOrDefault(regionCode, regionCode);
        }

        return new GymInfoRow(
                gymInfo.getGymId().longValue(),
                gymInfo.getGymName(),
                gymInfo.getServiceStatus().name(),
                gymInfo.getServiceStatus().getLabel(),
                gymInfo.getBusinessNumber(),
                gymInfo.getOwnerName(),
                gymInfo.getPhoneNumber(),
                buildAddress(gymInfo),
                regionName,
                gymInfo.getServiceStartDate(),
                gymInfo.getServiceEndDate(),
                gymInfo.getCreatedAt()
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
                        region -> region.getRegionCode(),
                        region -> region.getRegionName(),
                        (existing, replacement) -> existing
                ));
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
}
