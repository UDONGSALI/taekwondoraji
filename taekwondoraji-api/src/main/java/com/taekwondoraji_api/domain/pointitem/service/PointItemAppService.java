package com.taekwondoraji_api.domain.pointitem.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.gym.web.service.PointItemImageStorageService;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberGymPointHistory;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import com.taekwondoraji_api.domain.member.repository.MemberGymPointHistoryRepository;
import com.taekwondoraji_api.domain.pointitem.dto.PointItemAppResponse;
import com.taekwondoraji_api.domain.pointitem.dto.PointItemStoreResponse;
import com.taekwondoraji_api.domain.pointitem.entity.GymPointItem;
import com.taekwondoraji_api.domain.pointitem.entity.MemberPointItem;
import com.taekwondoraji_api.domain.pointitem.entity.MemberPointItemStatus;
import com.taekwondoraji_api.domain.pointitem.entity.PointItemStatus;
import com.taekwondoraji_api.domain.pointitem.repository.GymPointItemRepository;
import com.taekwondoraji_api.domain.pointitem.repository.MemberPointItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointItemAppService {

    private static final List<MemberPointItemStatus> REQUESTED_STATUSES = List.of(
            MemberPointItemStatus.hold,
            MemberPointItemStatus.used
    );

    private final MemberGymMapRepository memberGymMapRepository;
    private final GymPointItemRepository gymPointItemRepository;
    private final MemberPointItemRepository memberPointItemRepository;
    private final MemberGymPointHistoryRepository memberGymPointHistoryRepository;
    private final PointItemImageStorageService pointItemImageStorageService;

    public PointItemStoreResponse getStore(Integer memberGymMapId) {
        MemberGymMap memberGymMap = findMemberGymMap(memberGymMapId);
        Map<Integer, MemberPointItem> memberPointItemByItemId = memberPointItemRepository
                .findAllByMemberGymMapId(memberGymMapId)
                .stream()
                .filter(memberPointItem -> memberPointItem.getItemStatus() != MemberPointItemStatus.cancel)
                .collect(Collectors.toMap(
                        memberPointItem -> memberPointItem.getGymPointItem().getGymPointItemId(),
                        Function.identity(),
                        (latest, ignored) -> latest
                ));

        List<PointItemAppResponse> items = gymPointItemRepository
                .findAllByGymIdAndItemStatusOrderByGymPointItemIdDesc(
                        memberGymMap.getGym().getGymId(),
                        PointItemStatus.active
                )
                .stream()
                .map(pointItem -> toResponse(pointItem, memberPointItemByItemId.get(pointItem.getGymPointItemId())))
                .toList();

        return new PointItemStoreResponse(memberGymMap.getPoint(), items);
    }

    @Transactional
    public PointItemStoreResponse purchase(Integer memberGymMapId, Integer gymPointItemId) {
        MemberGymMap memberGymMap = findMemberGymMapForUpdate(memberGymMapId);
        GymPointItem pointItem = gymPointItemRepository
                .findByGymPointItemIdAndGymIdAndItemStatus(
                        gymPointItemId,
                        memberGymMap.getGym().getGymId(),
                        PointItemStatus.active
                )
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        var existingItem = memberPointItemRepository
                .findFirstByMemberGymMap_MemberGymMapIdAndGymPointItem_GymPointItemIdAndItemStatusInOrderByMemberPointItemIdDesc(
                        memberGymMapId,
                        gymPointItemId,
                        REQUESTED_STATUSES
                );

        if (existingItem.isPresent()) {
            return getStore(memberGymMapId);
        }

        int currentPoint = memberGymMap.getPoint() == null ? 0 : memberGymMap.getPoint();
        int itemPoint = pointItem.getPoint() == null ? 0 : pointItem.getPoint();
        if (currentPoint < itemPoint) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        memberGymMap.updatePoint(currentPoint - itemPoint);
        memberGymPointHistoryRepository.save(MemberGymPointHistory.usePointItem(
                memberGymMap,
                itemPoint,
                memberGymMap.getMemberInfo().getMemberId(),
                "포인트 아이템 구매: " + pointItem.getName()
        ));
        memberPointItemRepository.save(MemberPointItem.hold(memberGymMap, pointItem));

        return getStore(memberGymMapId);
    }

    private MemberGymMap findMemberGymMap(Integer memberGymMapId) {
        return memberGymMapRepository.findById(memberGymMapId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
    }

    private MemberGymMap findMemberGymMapForUpdate(Integer memberGymMapId) {
        return memberGymMapRepository.findByMemberGymMapIdForUpdate(memberGymMapId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
    }

    private PointItemAppResponse toResponse(GymPointItem pointItem, MemberPointItem memberPointItem) {
        return PointItemAppResponse.from(
                pointItem,
                memberPointItem,
                pointItemImageStorageService.findImageUrl(pointItem.getGymPointItemId())
        );
    }
}
