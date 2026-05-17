package com.taekwondoraji_api.domain.gym.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberDailyQuestParam {

    private LocalDate questDate;
}
