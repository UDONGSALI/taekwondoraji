package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.code.BeltCode;
import com.taekwondoraji_api.domain.dailyquest.entity.DailyQuestStatus;
import com.taekwondoraji_api.domain.dailyquest.entity.MemberDailyQuest;
import com.taekwondoraji_api.domain.dailyquest.repository.MemberDailyQuestRepository;
import com.taekwondoraji_api.domain.gym.web.dto.MemberDailyQuestInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.MemberDailyQuestMemberRow;
import com.taekwondoraji_api.domain.gym.web.dto.MemberDailyQuestParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymMemberDailyQuestPageService {

    private final GymDailyQuestPageService gymDailyQuestPageService;
    private final MemberDailyQuestRepository memberDailyQuestRepository;

    public MemberDailyQuestInfoPage getMemberDailyQuestInfoPage(Integer gymId, MemberDailyQuestParam param) {
        LocalDate questDate = param.getQuestDate() == null ? LocalDate.now() : param.getQuestDate();
        List<MemberDailyQuest> memberDailyQuests = memberDailyQuestRepository.findAllByGymIdAndQuestDate(
                gymId,
                questDate
        );

        return new MemberDailyQuestInfoPage(
                questDate,
                gymDailyQuestPageService.getDailyQuests(gymId, questDate),
                IntStream.range(0, memberDailyQuests.size())
                        .mapToObj(index -> toMemberRow(index + 1, memberDailyQuests.get(index)))
                        .toList()
        );
    }

    private MemberDailyQuestMemberRow toMemberRow(int no, MemberDailyQuest memberDailyQuest) {
        var memberGymMap = memberDailyQuest.getMemberGymMap();
        var dailyQuest = memberDailyQuest.getGymDailyQuest();

        return new MemberDailyQuestMemberRow(
                no,
                memberDailyQuest.getMemberDailyQuestId(),
                memberGymMap.getMemberInfo().getMemberName(),
                memberGymMap.getMemberStatus().getLabel(),
                BeltCode.label(memberGymMap.getBeltName()),
                dailyQuest.getName(),
                dailyQuest.getPoint(),
                memberDailyQuest.getQuestStatus().getLabel(),
                memberDailyQuest.getQuestStatus().name(),
                memberDailyQuest.getCompletedAt(),
                memberDailyQuest.getQuestStatus() == DailyQuestStatus.progress
        );
    }
}
