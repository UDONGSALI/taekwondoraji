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

    Optional<MemberGoal> findFirstByMemberGymMap_MemberGymMapIdAndGoal_GoalIdAndGoalStatusInOrderByMemberGoalIdDesc(
            Integer memberGymMapId,
            Integer goalId,
            List<GoalStatus> goalStatuses
    );
}
