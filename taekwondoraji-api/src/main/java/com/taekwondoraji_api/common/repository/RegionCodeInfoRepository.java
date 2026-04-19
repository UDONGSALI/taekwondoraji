package com.taekwondoraji_api.common.repository;

import com.taekwondoraji_api.common.entity.RegionCodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionCodeInfoRepository extends JpaRepository<RegionCodeInfo, String> {
}
