package com.taekwondoraji_api.domain.member.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.member.dto.MemberProfileResponse;
import com.taekwondoraji_api.domain.member.dto.MemberProfileUpdateRequest;
import com.taekwondoraji_api.domain.member.entity.MemberInfo;
import com.taekwondoraji_api.domain.member.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberProfileService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberProfileImageStorageService memberProfileImageStorageService;

    @Transactional
    public MemberProfileResponse updateProfile(Integer memberId, MemberProfileUpdateRequest request) {
        MemberInfo memberInfo = findMemberInfo(memberId);
        memberInfo.updateProfile(
                request.memberName().trim(),
                request.age(),
                trimToNull(request.phoneNumber()),
                trimToNull(request.motto())
        );

        return toResponse(memberInfo);
    }

    @Transactional
    public MemberProfileResponse updateProfileImage(Integer memberId, MultipartFile imageFile) {
        MemberInfo memberInfo = findMemberInfo(memberId);
        memberProfileImageStorageService.save(memberId, imageFile);
        return toResponse(memberInfo);
    }

    private MemberInfo findMemberInfo(Integer memberId) {
        return memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private MemberProfileResponse toResponse(MemberInfo memberInfo) {
        return MemberProfileResponse.from(
                memberInfo,
                memberProfileImageStorageService.findImageUrl(memberInfo.getMemberId())
        );
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
