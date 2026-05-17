package com.taekwondoraji_api.domain.gym.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyQuestCreateForm {

    @NotNull(message = "퀘스트 날짜는 필수입니다.")
    private LocalDate questDate = LocalDate.now();

    @NotBlank(message = "퀘스트 이름은 필수입니다.")
    @Size(max = 50, message = "퀘스트 이름은 50자 이하로 입력해 주세요.")
    private String name;

    @Size(max = 255, message = "퀘스트 설명은 255자 이하로 입력해 주세요.")
    private String description;

    @Size(max = 255, message = "설명 링크는 255자 이하로 입력해 주세요.")
    private String link;

    @NotNull(message = "포인트는 필수입니다.")
    @Min(value = 0, message = "포인트는 0 이상이어야 합니다.")
    @Max(value = 999999, message = "포인트 값이 너무 큽니다.")
    private Integer point = 0;
}
