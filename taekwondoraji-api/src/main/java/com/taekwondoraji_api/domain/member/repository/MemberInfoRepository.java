package com.taekwondoraji_api.domain.member.repository;

import com.taekwondoraji_api.domain.member.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Integer> {

    boolean existsByLoginId(String loginId);

    Optional<MemberInfo> findByLoginId(String loginId);
}
