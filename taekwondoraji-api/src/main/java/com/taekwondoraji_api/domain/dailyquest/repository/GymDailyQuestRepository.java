package com.taekwondoraji_api.domain.dailyquest.repository;

import com.taekwondoraji_api.domain.dailyquest.entity.GymDailyQuest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GymDailyQuestRepository extends JpaRepository<GymDailyQuest, Integer> {

    List<GymDailyQuest> findAllByGymIdOrderByQuestDateDescGymDailyQuestIdDesc(Integer gymId);

    List<GymDailyQuest> findAllByGymIdAndQuestDateOrderByGymDailyQuestIdDesc(Integer gymId, LocalDate questDate);

    Optional<GymDailyQuest> findByGymDailyQuestIdAndGymId(Integer gymDailyQuestId, Integer gymId);

    long countByGymId(Integer gymId);

    long countByGymIdAndQuestDate(Integer gymId, LocalDate questDate);
}
