package com.taekwondoraji_api.domain.member.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.member.dto.MemberLoginRequest;
import com.taekwondoraji_api.domain.member.dto.MemberLoginResponse;
import com.taekwondoraji_api.domain.member.entity.MemberInfo;
import com.taekwondoraji_api.domain.member.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberLoginService {

    private final MemberInfoRepository memberInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberLoginResponse login(MemberLoginRequest request) {
        MemberInfo memberInfo = memberInfoRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_LOGIN_INFO));

        if (!passwordEncoder.matches(request.loginPassword(), memberInfo.getLoginPassword())) {
            throw new BusinessException(ErrorCode.INVALID_LOGIN_INFO);
        }

        return MemberLoginResponse.from(memberInfo);
    }
}
