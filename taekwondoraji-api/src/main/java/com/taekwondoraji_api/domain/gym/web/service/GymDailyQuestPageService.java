package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.domain.dailyquest.entity.GymDailyQuest;
import com.taekwondoraji_api.domain.dailyquest.repository.GymDailyQuestRepository;
import com.taekwondoraji_api.domain.gym.web.dto.DailyQuestInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.DailyQuestInfoRow;
import com.taekwondoraji_api.domain.gym.web.dto.DailyQuestInfoSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymDailyQuestPageService {

    private final GymDailyQuestRepository gymDailyQuestRepository;

    public DailyQuestInfoPage getDailyQuestInfoPage(Integer gymId) {
        List<DailyQuestInfoRow> list = gymDailyQuestRepository.findAllByGymIdOrderByQuestDateDescGymDailyQuestIdDesc(gymId)
                .stream()
                .map(this::toRow)
                .toList();

        return new DailyQuestInfoPage(
                list,
                new DailyQuestInfoSummary(
                        (int) gymDailyQuestRepository.countByGymId(gymId),
                        (int) gymDailyQuestRepository.countByGymIdAndQuestDate(gymId, LocalDate.now())
                )
        );
    }

    public List<DailyQuestInfoRow> getDailyQuests(Integer gymId, LocalDate questDate) {
        return gymDailyQuestRepository.findAllByGymIdAndQuestDateOrderByGymDailyQuestIdDesc(gymId, questDate)
                .stream()
                .map(this::toRow)
                .toList();
    }

    private DailyQuestInfoRow toRow(GymDailyQuest dailyQuest) {
        return new DailyQuestInfoRow(
                dailyQuest.getGymDailyQuestId(),
                dailyQuest.getQuestDate(),
                dailyQuest.getName(),
                dailyQuest.getDescription(),
                dailyQuest.getLink(),
                dailyQuest.getPoint(),
                dailyQuest.getCreatedAt()
        );
    }
}
