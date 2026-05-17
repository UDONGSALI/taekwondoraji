package com.taekwondoraji_api.domain.goal.repository;

import com.taekwondoraji_api.domain.goal.entity.Goal;
import com.taekwondoraji_api.domain.goal.entity.GoalSource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Integer> {

    long countByGoalSource(GoalSource goalSource);

    long countByGoalSourceAndCreatedGymId(GoalSource goalSource, Integer createdGymId);

    Optional<Goal> findByGoalIdAndGoalSourceAndCreatedGymId(Integer goalId, GoalSource goalSource, Integer createdGymId);

    @Query("""
            select g
            from Goal g
            where g.goalSource = :commonSource
               or (g.goalSource = :customSource and g.createdGymId = :gymId)
            order by g.goalId desc
            """)
    List<Goal> findVisibleGoalsForGym(
            @Param("gymId") Integer gymId,
            @Param("commonSource") GoalSource commonSource,
            @Param("customSource") GoalSource customSource
    );

    List<Goal> findAllByOrderByPointAscGoalIdDesc();

    List<Goal> findAllByCategoryOrderByPointAscGoalIdDesc(String category);
}
