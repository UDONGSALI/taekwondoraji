package com.taekwondoraji_api.domain.goal.repository;

import com.taekwondoraji_api.domain.goal.entity.Goal;
import com.taekwondoraji_api.domain.goal.entity.GoalSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Integer> {

    long countByGoalSource(GoalSource goalSource);
}
