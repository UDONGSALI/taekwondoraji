package com.taekwondoraji_api.domain.admin.goal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalCreateForm {

    @NotBlank(message = "목표 이름은 필수입니다.")
    @Size(max = 20, message = "목표 이름은 20자 이하로 입력해주세요.")
    private String name;

    @Size(max = 100, message = "목표 설명은 100자 이하로 입력해주세요.")
    private String description;

    @Size(max = 255, message = "설명 링크는 255자 이하로 입력해주세요.")
    private String link;

    @NotBlank(message = "목표 카테고리는 필수입니다.")
    private String category;

    @NotNull(message = "포인트는 필수입니다.")
    @Min(value = 0, message = "포인트는 0 이상이어야 합니다.")
    @Max(value = 999999, message = "포인트 값이 너무 큽니다.")
    private Integer point;
}
