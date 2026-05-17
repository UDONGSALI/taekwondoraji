package com.taekwondoraji_api.domain.member.repository;

import com.taekwondoraji_api.domain.member.entity.MemberGymBeltHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberGymBeltHistoryRepository extends JpaRepository<MemberGymBeltHistory, Integer> {

    @Query("""
            select history
            from MemberGymBeltHistory history
            where history.memberGymMap.memberGymMapId = :memberGymMapId
              and history.memberGymMap.gym.gymId = :gymId
            order by history.beltHistoryId desc
            """)
    List<MemberGymBeltHistory> findAllByMemberGymMapIdAndGymId(
            @Param("memberGymMapId") Integer memberGymMapId,
            @Param("gymId") Integer gymId
    );
}
