package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.dailyquest.entity.GymDailyQuest;
import com.taekwondoraji_api.domain.dailyquest.repository.GymDailyQuestRepository;
import com.taekwondoraji_api.domain.gym.web.dto.DailyQuestCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GymDailyQuestCommandService {

    private final GymDailyQuestRepository gymDailyQuestRepository;

    public void createDailyQuest(Integer gymId, DailyQuestCreateForm form) {
        GymDailyQuest dailyQuest = new GymDailyQuest(
                gymId,
                form.getQuestDate(),
                form.getName(),
                blankToNull(form.getDescription()),
                blankToNull(form.getLink()),
                form.getPoint()
        );

        gymDailyQuestRepository.save(dailyQuest);
    }

    public void deleteDailyQuest(Integer gymId, Integer gymDailyQuestId) {
        GymDailyQuest dailyQuest = gymDailyQuestRepository
                .findByGymDailyQuestIdAndGymId(gymDailyQuestId, gymId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        gymDailyQuestRepository.delete(dailyQuest);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value;
    }
}
