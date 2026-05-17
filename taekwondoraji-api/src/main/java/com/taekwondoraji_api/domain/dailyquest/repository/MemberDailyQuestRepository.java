package com.taekwondoraji_api.domain.dailyquest.repository;

import com.taekwondoraji_api.domain.dailyquest.entity.DailyQuestStatus;
import com.taekwondoraji_api.domain.dailyquest.entity.MemberDailyQuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberDailyQuestRepository extends JpaRepository<MemberDailyQuest, Integer> {

    @Query("""
            select mdq
            from MemberDailyQuest mdq
            join fetch mdq.memberGymMap memberGymMap
            join fetch memberGymMap.memberInfo
            join fetch mdq.gymDailyQuest dailyQuest
            where memberGymMap.gym.gymId = :gymId
              and dailyQuest.questDate = :questDate
            order by dailyQuest.gymDailyQuestId desc, memberGymMap.memberGymMapId desc
            """)
    List<MemberDailyQuest> findAllByGymIdAndQuestDate(
            @Param("gymId") Integer gymId,
            @Param("questDate") LocalDate questDate
    );

    List<MemberDailyQuest> findAllByMemberGymMap_MemberGymMapIdOrderByMemberDailyQuestIdDesc(Integer memberGymMapId);

    @Query("""
            select mdq
            from MemberDailyQuest mdq
            join fetch mdq.memberGymMap memberGymMap
            join fetch mdq.gymDailyQuest dailyQuest
            where mdq.memberDailyQuestId = :memberDailyQuestId
              and memberGymMap.gym.gymId = :gymId
              and mdq.questStatus = :questStatus
            """)
    Optional<MemberDailyQuest> findByIdAndGymIdAndQuestStatus(
            @Param("memberDailyQuestId") Integer memberDailyQuestId,
            @Param("gymId") Integer gymId,
            @Param("questStatus") DailyQuestStatus questStatus
    );

    void deleteAllByGymDailyQuest_GymDailyQuestId(Integer gymDailyQuestId);

    Optional<MemberDailyQuest> findByMemberDailyQuestIdAndMemberGymMap_MemberGymMapIdAndQuestStatus(
            Integer memberDailyQuestId,
            Integer memberGymMapId,
            DailyQuestStatus questStatus
    );
}
