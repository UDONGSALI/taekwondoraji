package com.taekwondoraji_api.domain.member.repository;

import com.taekwondoraji_api.domain.member.entity.MemberGymPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberGymPointHistoryRepository extends JpaRepository<MemberGymPointHistory, Integer> {

    @Query("""
            select history
            from MemberGymPointHistory history
            where history.memberGymMap.memberGymMapId = :memberGymMapId
              and history.memberGymMap.gym.gymId = :gymId
            order by history.pointHistoryId desc
            """)
    List<MemberGymPointHistory> findAllByMemberGymMapIdAndGymId(
            @Param("memberGymMapId") Integer memberGymMapId,
            @Param("gymId") Integer gymId
    );
}
