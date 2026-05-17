package com.taekwondoraji_api.domain.auth.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSignupForm {

    @NotBlank
    @Size(max = 50)
    private String loginId;

    @NotBlank
    @Size(min = 4, max = 100)
    private String loginPassword;

    @NotBlank
    @Size(max = 50)
    private String memberName;

    private Integer age;

    @Size(max = 20)
    private String phoneNumber;
}
