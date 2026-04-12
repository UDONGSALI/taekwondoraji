package com.taekwondoraji_api.domain.gym.repository;

import com.taekwondoraji_api.domain.gym.entity.GymInfo;
import com.taekwondoraji_api.domain.gym.entity.GymServiceStatus;
import org.springframework.data.jpa.domain.Specification;

public final class GymInfoSpecification {

    private GymInfoSpecification() {
    }

    public static Specification<GymInfo> serviceStatusEquals(GymServiceStatus serviceStatus) {
        return (root, query, criteriaBuilder) ->
                serviceStatus == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("serviceStatus"), serviceStatus);
    }

    public static Specification<GymInfo> keywordContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String searchKeyword = "%" + keyword.trim() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("gymName"), searchKeyword),
                    criteriaBuilder.like(root.get("ownerName"), searchKeyword),
                    criteriaBuilder.like(root.get("businessNumber"), searchKeyword)
            );
        };
    }

    public static Specification<GymInfo> regionCodeEquals(String regionCode) {
        return (root, query, criteriaBuilder) ->
                regionCode == null || regionCode.isBlank()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("regionCode"), regionCode.trim());
    }

    public static Specification<GymInfo> isActiveEquals(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                isActive == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("isActive"), isActive);
    }
}
