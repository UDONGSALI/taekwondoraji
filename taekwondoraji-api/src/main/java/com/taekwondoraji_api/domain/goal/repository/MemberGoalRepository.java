package com.taekwondoraji_api.domain.goal.repository;

import com.taekwondoraji_api.domain.goal.dto.GoalCategoryPointSum;
import com.taekwondoraji_api.domain.goal.entity.GoalStatus;
import com.taekwondoraji_api.domain.goal.entity.MemberGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberGoalRepository extends JpaRepository<MemberGoal, Integer> {

    @Query("""
            select new com.taekwondoraji_api.domain.goal.dto.GoalCategoryPointSum(
                mg.goal.category,
                sum(mg.goal.point)
            )
            from MemberGoal mg
            where mg.memberGymMap.memberGymMapId = :memberGymMapId
              and mg.goalStatus = com.taekwondoraji_api.domain.goal.entity.GoalStatus.complete
            group by mg.goal.category
            """)
    List<GoalCategoryPointSum> sumCompletedPointByCategory(@Param("memberGymMapId") Integer memberGymMapId);

    List<MemberGoal> findAllByMemberGymMap_MemberGymMapIdOrderByMemberGoalIdDesc(Integer memberGymMapId);

    @Query("""
            select mg
            from MemberGoal mg
            join fetch mg.memberGymMap memberGymMap
            join fetch memberGymMap.memberInfo
            join fetch mg.goal
            where memberGymMap.gym.gymId = :gymId
              and (:goalStatus is null or mg.goalStatus = :goalStatus)
              and (:memberId is null or memberGymMap.memberInfo.memberId = :memberId)
              and (:category is null or mg.goal.category = :category)
            order by mg.memberGoalId desc
            """)
    List<MemberGoal> findAllByGymIdOrderByMemberGoalIdDesc(
            @Param("gymId") Integer gymId,
            @Param("goalStatus") GoalStatus goalStatus,
            @Param("memberId") Integer memberId,
            @Param("category") String category
    );

    @Query("""
            select count(mg)
            from MemberGoal mg
            where mg.memberGymMap.gym.gymId = :gymId
            """)
    long countByGymId(@Param("gymId") Integer gymId);

    @Query("""
            select count(mg)
            from MemberGoal mg
            where mg.memberGymMap.gym.gymId = :gymId
              and mg.goalStatus = :goalStatus
            """)
    long countByGymIdAndGoalStatus(
            @Param("gymId") Integer gymId,
            @Param("goalStatus") GoalStatus goalStatus
    );

    @Query("""
            select mg
            from MemberGoal mg
            where mg.memberGoalId = :memberGoalId
              and mg.memberGymMap.gym.gymId = :gymId
              and mg.goalStatus = :goalStatus
            """)
    Optional<MemberGoal> findByIdAndGymIdAndGoalStatus(
            @Param("memberGoalId") Integer memberGoalId,
            @Param("gymId") Integer gymId,
            @Param("goalStatus") GoalStatus goalStatus
    );

    @Query("""
            select mg
            from MemberGoal mg
            where mg.memberGoalId in :memberGoalIds
              and mg.memberGymMap.gym.gymId = :gymId
              and mg.goalStatus = :goalStatus
            """)
    List<MemberGoal> findAllByIdsAndGymIdAndGoalStatus(
            @Param("memberGoalIds") List<Integer> memberGoalIds,
            @Param("gymId") Integer gymId,
            @Param("goalStatus") GoalStatus goalStatus
    );

    Optional<MemberGoal> findFirstByMemberGymMap_MemberGymMapIdAndGoal_GoalIdAndGoalStatusInOrderByMemberGoalIdDesc(
            Integer memberGymMapId,
            Integer goalId,
            List<GoalStatus> goalStatuses
    );

    Optional<MemberGoal> findByMemberGoalIdAndMemberGymMap_MemberGymMapIdAndGoalStatus(
            Integer memberGoalId,
            Integer memberGymMapId,
            GoalStatus goalStatus
    );
}
