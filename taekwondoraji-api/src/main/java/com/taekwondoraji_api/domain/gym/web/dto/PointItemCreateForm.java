package com.taekwondoraji_api.domain.gym.web.dto;

import com.taekwondoraji_api.domain.pointitem.entity.PointItemStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointItemCreateForm {

    @NotBlank(message = "아이템 이름은 필수입니다.")
    @Size(max = 50, message = "아이템 이름은 50자 이하로 입력해 주세요.")
    private String name;

    @Size(max = 255, message = "아이템 설명은 255자 이하로 입력해 주세요.")
    private String description;

    @Size(max = 255, message = "설명 링크는 255자 이하로 입력해 주세요.")
    private String link;

    @NotNull(message = "사용 포인트는 필수입니다.")
    @Min(value = 0, message = "사용 포인트는 0 이상이어야 합니다.")
    @Max(value = 999999, message = "포인트 값이 너무 큽니다.")
    private Integer point = 0;

    @NotNull(message = "아이템 상태는 필수입니다.")
    private PointItemStatus itemStatus = PointItemStatus.active;
}
