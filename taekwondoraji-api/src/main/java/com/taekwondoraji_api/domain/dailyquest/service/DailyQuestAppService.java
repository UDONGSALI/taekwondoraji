package com.taekwondoraji_api.domain.dailyquest.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.dailyquest.dto.DailyQuestListResponse;
import com.taekwondoraji_api.domain.dailyquest.entity.DailyQuestStatus;
import com.taekwondoraji_api.domain.dailyquest.entity.GymDailyQuest;
import com.taekwondoraji_api.domain.dailyquest.entity.MemberDailyQuest;
import com.taekwondoraji_api.domain.dailyquest.repository.GymDailyQuestRepository;
import com.taekwondoraji_api.domain.dailyquest.repository.MemberDailyQuestRepository;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyQuestAppService {

    private final GymDailyQuestRepository gymDailyQuestRepository;
    private final MemberDailyQuestRepository memberDailyQuestRepository;
    private final MemberGymMapRepository memberGymMapRepository;

    public List<DailyQuestListResponse> getDailyQuests(Integer memberGymMapId) {
        MemberGymMap memberGymMap = memberGymMapRepository.findById(memberGymMapId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        Map<Integer, MemberDailyQuest> memberDailyQuestByQuestId = memberDailyQuestRepository
                .findAllByMemberGymMap_MemberGymMapIdOrderByMemberDailyQuestIdDesc(memberGymMapId)
                .stream()
                .filter(memberDailyQuest -> memberDailyQuest.getQuestStatus() != DailyQuestStatus.cancel)
                .collect(Collectors.toMap(
                        memberDailyQuest -> memberDailyQuest.getGymDailyQuest().getGymDailyQuestId(),
                        Function.identity(),
                        (latest, ignored) -> latest
                ));

        return gymDailyQuestRepository.findAllByGymIdOrderByQuestDateDescGymDailyQuestIdDesc(
                        memberGymMap.getGym().getGymId()
                )
                .stream()
                .map(dailyQuest -> {
                    MemberDailyQuest memberDailyQuest = memberDailyQuestByQuestId.get(dailyQuest.getGymDailyQuestId());
                    return memberDailyQuest == null
                            ? DailyQuestListResponse.waiting(dailyQuest)
                            : DailyQuestListResponse.from(memberDailyQuest);
                })
                .toList();
    }

    @Transactional
    public DailyQuestListResponse applyDailyQuest(Integer memberGymMapId, Integer dailyQuestId) {
        MemberGymMap memberGymMap = memberGymMapRepository.findById(memberGymMapId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
        GymDailyQuest dailyQuest = gymDailyQuestRepository.findByGymDailyQuestIdAndGymId(
                        dailyQuestId,
                        memberGymMap.getGym().getGymId()
                )
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        return memberDailyQuestRepository
                .findAllByMemberGymMap_MemberGymMapIdOrderByMemberDailyQuestIdDesc(memberGymMapId)
                .stream()
                .filter(memberDailyQuest -> memberDailyQuest.getGymDailyQuest().getGymDailyQuestId().equals(dailyQuestId))
                .filter(memberDailyQuest -> memberDailyQuest.getQuestStatus() != DailyQuestStatus.cancel)
                .findFirst()
                .map(DailyQuestListResponse::from)
                .orElseGet(() -> DailyQuestListResponse.from(
                        memberDailyQuestRepository.save(MemberDailyQuest.create(memberGymMap, dailyQuest))
                ));
    }

    @Transactional
    public void deleteDailyQuestApplication(Integer memberGymMapId, Integer memberDailyQuestId) {
        MemberDailyQuest memberDailyQuest = memberDailyQuestRepository
                .findByMemberDailyQuestIdAndMemberGymMap_MemberGymMapIdAndQuestStatus(
                        memberDailyQuestId,
                        memberGymMapId,
                        DailyQuestStatus.progress
                )
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        memberDailyQuestRepository.delete(memberDailyQuest);
    }
}
