package com.taekwondoraji_api.domain.member.repository;

import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberRole;
import com.taekwondoraji_api.domain.member.entity.MemberStatus;
import org.springframework.data.jpa.domain.Specification;

public final class MemberGymMapSpecification {

    private MemberGymMapSpecification() {
    }

    public static Specification<MemberGymMap> memberRoleEquals(MemberRole memberRole) {
        return (root, query, criteriaBuilder) ->
                memberRole == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("memberRole"), memberRole);
    }

    public static Specification<MemberGymMap> memberStatusEquals(MemberStatus memberStatus) {
        return (root, query, criteriaBuilder) ->
                memberStatus == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("memberStatus"), memberStatus);
    }
}
