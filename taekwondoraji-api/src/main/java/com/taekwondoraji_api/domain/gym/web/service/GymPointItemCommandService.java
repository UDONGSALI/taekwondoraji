package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.gym.web.dto.PointItemCreateForm;
import com.taekwondoraji_api.domain.pointitem.entity.GymPointItem;
import com.taekwondoraji_api.domain.pointitem.entity.PointItemStatus;
import com.taekwondoraji_api.domain.pointitem.repository.GymPointItemRepository;
import com.taekwondoraji_api.domain.pointitem.repository.MemberPointItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class GymPointItemCommandService {

    private final GymPointItemRepository gymPointItemRepository;
    private final MemberPointItemRepository memberPointItemRepository;
    private final PointItemImageStorageService pointItemImageStorageService;

    public void createPointItem(Integer gymId, PointItemCreateForm form, MultipartFile imageFile) {
        GymPointItem pointItem = new GymPointItem(
                gymId,
                form.getName(),
                blankToNull(form.getDescription()),
                blankToNull(form.getLink()),
                form.getPoint(),
                form.getItemStatus()
        );

        GymPointItem savedPointItem = gymPointItemRepository.save(pointItem);
        pointItemImageStorageService.save(savedPointItem.getGymPointItemId(), imageFile);
    }

    public void updatePointItemStatus(Integer gymId, Integer gymPointItemId, PointItemStatus itemStatus) {
        GymPointItem pointItem = gymPointItemRepository.findByGymPointItemIdAndGymId(gymPointItemId, gymId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        pointItem.updateItemStatus(itemStatus);
    }

    public void deletePointItem(Integer gymId, Integer gymPointItemId) {
        GymPointItem pointItem = gymPointItemRepository.findByGymPointItemIdAndGymId(gymPointItemId, gymId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        memberPointItemRepository.deleteAllByGymPointItem_GymPointItemId(gymPointItemId);
        gymPointItemRepository.delete(pointItem);
        pointItemImageStorageService.delete(gymPointItemId);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value;
    }
}
