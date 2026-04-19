package com.taekwondoraji_api.domain.gym.repository;

import com.taekwondoraji_api.domain.gym.entity.GymInfo;
import com.taekwondoraji_api.domain.gym.entity.GymServiceStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GymInfoRepository extends JpaRepository<GymInfo, Integer>, JpaSpecificationExecutor<GymInfo> {

    default List<GymInfo> findAllBySpec(Specification<GymInfo> spec, Sort sort) {
        return findAll(spec, sort);
    }

    long countByServiceStatus(GymServiceStatus serviceStatus);
}
