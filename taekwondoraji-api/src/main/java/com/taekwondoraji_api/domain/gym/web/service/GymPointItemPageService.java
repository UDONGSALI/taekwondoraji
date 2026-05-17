package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.code.BeltCode;
import com.taekwondoraji_api.domain.gym.web.dto.MemberPointItemRow;
import com.taekwondoraji_api.domain.gym.web.dto.PointItemInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.PointItemInfoRow;
import com.taekwondoraji_api.domain.gym.web.dto.PointItemInfoSummary;
import com.taekwondoraji_api.domain.pointitem.entity.MemberPointItem;
import com.taekwondoraji_api.domain.pointitem.entity.MemberPointItemStatus;
import com.taekwondoraji_api.domain.pointitem.entity.GymPointItem;
import com.taekwondoraji_api.domain.pointitem.repository.GymPointItemRepository;
import com.taekwondoraji_api.domain.pointitem.repository.MemberPointItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymPointItemPageService {

    private final GymPointItemRepository gymPointItemRepository;
    private final MemberPointItemRepository memberPointItemRepository;
    private final PointItemImageStorageService pointItemImageStorageService;

    public PointItemInfoPage getPointItemInfoPage(Integer gymId) {
        List<GymPointItem> pointItems = gymPointItemRepository.findAllByGymIdOrderByGymPointItemIdDesc(gymId);
        List<MemberPointItem> memberPointItems = memberPointItemRepository.findAllByGymId(gymId);

        return new PointItemInfoPage(
                IntStream.range(0, pointItems.size())
                        .mapToObj(index -> toItemRow(index + 1, pointItems.get(index)))
                        .toList(),
                IntStream.range(0, memberPointItems.size())
                        .mapToObj(index -> toMemberRow(index + 1, memberPointItems.get(index)))
                        .toList(),
                new PointItemInfoSummary(
                        pointItems.size(),
                        countStatus(memberPointItems, MemberPointItemStatus.hold),
                        countStatus(memberPointItems, MemberPointItemStatus.used),
                        countStatus(memberPointItems, MemberPointItemStatus.cancel)
                )
        );
    }

    private PointItemInfoRow toItemRow(int no, GymPointItem pointItem) {
        return new PointItemInfoRow(
                no,
                pointItem.getGymPointItemId(),
                pointItem.getName(),
                pointItem.getDescription(),
                pointItem.getLink(),
                pointItem.getPoint(),
                pointItem.getItemStatus().name(),
                pointItem.getItemStatus().getLabel(),
                pointItemImageStorageService.findImageUrl(pointItem.getGymPointItemId()),
                pointItem.getCreatedAt()
        );
    }

    private MemberPointItemRow toMemberRow(int no, MemberPointItem memberPointItem) {
        var memberGymMap = memberPointItem.getMemberGymMap();
        var pointItem = memberPointItem.getGymPointItem();

        return new MemberPointItemRow(
                no,
                memberPointItem.getMemberPointItemId(),
                memberGymMap.getMemberInfo().getMemberName(),
                memberGymMap.getMemberStatus().getLabel(),
                BeltCode.label(memberGymMap.getBeltName()),
                pointItem.getName(),
                pointItem.getPoint(),
                memberPointItem.getItemStatus().name(),
                memberPointItem.getItemStatus().getLabel(),
                memberPointItem.getCreatedAt(),
                memberPointItem.getUsedAt()
        );
    }

    private int countStatus(List<MemberPointItem> memberPointItems, MemberPointItemStatus status) {
        return (int) memberPointItems.stream()
                .filter(memberPointItem -> memberPointItem.getItemStatus() == status)
                .count();
    }
}
