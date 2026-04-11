package com.taekwondoraji_api.domain.member.repository;

import com.taekwondoraji_api.domain.member.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager entityManager;

    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(entityManager.find(Member.class, memberId));
    }
}
