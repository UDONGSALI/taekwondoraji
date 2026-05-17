package com.taekwondoraji_api.domain.pointitem.repository;

import com.taekwondoraji_api.domain.pointitem.entity.MemberPointItem;
import com.taekwondoraji_api.domain.pointitem.entity.MemberPointItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberPointItemRepository extends JpaRepository<MemberPointItem, Integer> {

    @Query("""
            select mpi
            from MemberPointItem mpi
            join fetch mpi.memberGymMap memberGymMap
            join fetch memberGymMap.memberInfo
            join fetch mpi.gymPointItem pointItem
            where memberGymMap.gym.gymId = :gymId
            order by mpi.memberPointItemId desc
            """)
    List<MemberPointItem> findAllByGymId(@Param("gymId") Integer gymId);

    @Query("""
            select mpi
            from MemberPointItem mpi
            join fetch mpi.gymPointItem pointItem
            where mpi.memberGymMap.memberGymMapId = :memberGymMapId
            order by mpi.memberPointItemId desc
            """)
    List<MemberPointItem> findAllByMemberGymMapId(@Param("memberGymMapId") Integer memberGymMapId);

    Optional<MemberPointItem> findFirstByMemberGymMap_MemberGymMapIdAndGymPointItem_GymPointItemIdAndItemStatusInOrderByMemberPointItemIdDesc(
            Integer memberGymMapId,
            Integer gymPointItemId,
            Collection<MemberPointItemStatus> itemStatuses
    );

    void deleteAllByGymPointItem_GymPointItemId(Integer gymPointItemId);
}
