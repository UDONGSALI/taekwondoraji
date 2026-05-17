package com.taekwondoraji_api.domain.pointitem.repository;

import com.taekwondoraji_api.domain.pointitem.entity.GymPointItem;
import com.taekwondoraji_api.domain.pointitem.entity.PointItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GymPointItemRepository extends JpaRepository<GymPointItem, Integer> {

    List<GymPointItem> findAllByGymIdOrderByGymPointItemIdDesc(Integer gymId);

    List<GymPointItem> findAllByGymIdAndItemStatusOrderByGymPointItemIdDesc(
            Integer gymId,
            PointItemStatus itemStatus
    );

    Optional<GymPointItem> findByGymPointItemIdAndGymId(Integer gymPointItemId, Integer gymId);

    Optional<GymPointItem> findByGymPointItemIdAndGymIdAndItemStatus(
            Integer gymPointItemId,
            Integer gymId,
            PointItemStatus itemStatus
    );

    long countByGymId(Integer gymId);
}
