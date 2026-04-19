package com.taekwondoraji_api.domain.admin.gym.dto;

import java.util.List;

public record GymMemberSection(
        int adminCount,
        int memberCount,
        List<GymMemberRow> adminList,
        List<GymMemberRow> memberList
) {
}
