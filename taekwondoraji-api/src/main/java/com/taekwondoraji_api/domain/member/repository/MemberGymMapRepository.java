package com.taekwondoraji_api.domain.member.repository;

import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MemberGymMapRepository extends JpaRepository<MemberGymMap, Integer>, JpaSpecificationExecutor<MemberGymMap> {

    @EntityGraph(attributePaths = {"memberInfo", "gym"})
    List<MemberGymMap> findAll(Specification<MemberGymMap> spec, Sort sort);

    @EntityGraph(attributePaths = "memberInfo")
    List<MemberGymMap> findAllByGym_GymIdOrderByMemberGymMapIdDesc(Integer gymId);

    long countByMemberStatus(MemberStatus memberStatus);

    default List<MemberGymMap> findAllBySpec(Specification<MemberGymMap> spec, Sort sort) {
        return findAll(spec, sort);
    }
}
