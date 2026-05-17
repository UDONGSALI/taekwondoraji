package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.dailyquest.entity.DailyQuestStatus;
import com.taekwondoraji_api.domain.dailyquest.entity.GymDailyQuest;
import com.taekwondoraji_api.domain.dailyquest.entity.MemberDailyQuest;
import com.taekwondoraji_api.domain.dailyquest.repository.MemberDailyQuestRepository;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.entity.MemberGymPointHistory;
import com.taekwondoraji_api.domain.member.repository.MemberGymPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GymMemberDailyQuestCommandService {

    private final MemberDailyQuestRepository memberDailyQuestRepository;
    private final MemberGymPointHistoryRepository memberGymPointHistoryRepository;

    public void completeMemberDailyQuest(Integer gymId, Integer memberDailyQuestId) {
        MemberDailyQuest memberDailyQuest = memberDailyQuestRepository
                .findByIdAndGymIdAndQuestStatus(memberDailyQuestId, gymId, DailyQuestStatus.progress)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        completeAndEarnPoint(memberDailyQuest);
    }

    private void completeAndEarnPoint(MemberDailyQuest memberDailyQuest) {
        memberDailyQuest.complete();

        MemberGymMap memberGymMap = memberDailyQuest.getMemberGymMap();
        GymDailyQuest dailyQuest = memberDailyQuest.getGymDailyQuest();
        Integer point = dailyQuest.getPoint() == null ? 0 : dailyQuest.getPoint();
        memberGymMap.addPoint(point);

        memberGymPointHistoryRepository.save(MemberGymPointHistory.earnDailyQuest(
                memberGymMap,
                point,
                null,
                "일퀘 완료: " + dailyQuest.getName()
        ));
    }
}
