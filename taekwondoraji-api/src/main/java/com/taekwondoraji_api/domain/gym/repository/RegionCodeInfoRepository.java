package com.taekwondoraji_api.domain.gym.repository;

import com.taekwondoraji_api.domain.gym.entity.RegionCodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionCodeInfoRepository extends JpaRepository<RegionCodeInfo, String> {
}
