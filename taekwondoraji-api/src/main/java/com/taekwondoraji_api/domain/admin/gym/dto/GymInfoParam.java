package com.taekwondoraji_api.domain.admin.gym.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GymInfoParam {

    private String keyword;
    private String serviceStatus;
    private String regionCode;
    private Integer page = 1;
    private Integer size = 20;
}
