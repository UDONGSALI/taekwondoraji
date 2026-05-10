package com.taekwondoraji_api.domain.member.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.member.dto.MemberSignupRequest;
import com.taekwondoraji_api.domain.member.dto.MemberSignupResponse;
import com.taekwondoraji_api.domain.member.entity.MemberInfo;
import com.taekwondoraji_api.domain.member.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSignupService {

    private final MemberInfoRepository memberInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberSignupResponse signup(MemberSignupRequest request) {
        if (memberInfoRepository.existsByLoginId(request.loginId())) {
            throw new BusinessException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        String encodedPassword = passwordEncoder.encode(request.loginPassword());
        MemberInfo memberInfo = MemberInfo.create(
                request.loginId(),
                encodedPassword,
                request.memberName(),
                request.age(),
                request.phoneNumber(),
                request.postalCode(),
                request.addressRoad(),
                request.addressDetail()
        );

        return MemberSignupResponse.from(memberInfoRepository.save(memberInfo));
    }
}
