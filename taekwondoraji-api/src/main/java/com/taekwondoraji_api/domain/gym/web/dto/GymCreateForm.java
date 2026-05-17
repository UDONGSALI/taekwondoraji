package com.taekwondoraji_api.domain.gym.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GymCreateForm {

    @NotBlank
    @Size(max = 100)
    private String gymName;

    @NotBlank
    @Size(min = 10, max = 10)
    private String businessNumber;

    @NotBlank
    @Size(max = 50)
    private String ownerName;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 10)
    private String postalCode;

    @Size(max = 255)
    private String addressRoad;

    @Size(max = 255)
    private String addressDetail;

    @Size(max = 20)
    private String regionCode;
}
