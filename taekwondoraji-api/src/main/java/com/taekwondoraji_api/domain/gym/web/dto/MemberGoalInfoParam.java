package com.taekwondoraji_api.domain.gym.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberGoalInfoParam {

    private String goalStatus;
    private Integer memberId;
    private String category;
}
