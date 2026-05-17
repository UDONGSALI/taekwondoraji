package com.taekwondoraji_api.domain.auth.web;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebLoginForm {

    @NotBlank
    private String loginId;

    private String loginPassword;

    private String redirectUri;
}
