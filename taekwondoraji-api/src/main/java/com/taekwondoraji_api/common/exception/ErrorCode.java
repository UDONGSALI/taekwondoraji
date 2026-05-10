package com.taekwondoraji_api.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 로그인 아이디입니다."),
    DUPLICATE_MEMBER_GYM(HttpStatus.CONFLICT, "이미 참여 신청한 도장입니다."),
    INVALID_LOGIN_INFO(HttpStatus.UNAUTHORIZED, "로그인 정보를 확인해 주세요."),
    GYM_NOT_FOUND(HttpStatus.NOT_FOUND, "도장 정보를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
