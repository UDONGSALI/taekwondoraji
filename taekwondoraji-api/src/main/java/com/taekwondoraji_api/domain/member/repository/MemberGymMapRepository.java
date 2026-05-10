package com.taekwondoraji_api.domain.member.repository;

import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MemberGymMapRepository extends JpaRepository<MemberGymMap, Integer>, JpaSpecificationExecutor<MemberGymMap> {

    @EntityGraph(attributePaths = {"memberInfo", "gym"})
    List<MemberGymMap> findAll(Specification<MemberGymMap> spec, Sort sort);

    @EntityGraph(attributePaths = "memberInfo")
    List<MemberGymMap> findAllByGym_GymIdOrderByMemberGymMapIdDesc(Integer gymId);

    @EntityGraph(attributePaths = {"memberInfo", "gym"})
    List<MemberGymMap> findAllByMemberInfo_MemberIdOrderByMemberGymMapIdDesc(Integer memberId);

    boolean existsByMemberInfo_MemberIdAndGym_GymId(Integer memberId, Integer gymId);

    Optional<MemberGymMap> findByGym_GymIdAndMemberInfo_MemberId(Integer gymId, Integer memberId);

    long countByMemberStatus(MemberStatus memberStatus);

    default List<MemberGymMap> findAllBySpec(Specification<MemberGymMap> spec, Sort sort) {
        return findAll(spec, sort);
    }
}
