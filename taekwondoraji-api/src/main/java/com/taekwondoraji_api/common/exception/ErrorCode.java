package com.taekwondoraji_api.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "\uC798\uBABB\uB41C \uC694\uCCAD\uC785\uB2C8\uB2E4."),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "\uC774\uBBF8 \uC0AC\uC6A9 \uC911\uC778 \uB85C\uADF8\uC778 \uC544\uC774\uB514\uC785\uB2C8\uB2E4."),
    DUPLICATE_MEMBER_GYM(HttpStatus.CONFLICT, "\uC774\uBBF8 \uCC38\uC5EC \uC2E0\uCCAD\uD55C \uB3C4\uC7A5\uC785\uB2C8\uB2E4."),
    INVALID_LOGIN_INFO(HttpStatus.UNAUTHORIZED, "\uB85C\uADF8\uC778 \uC815\uBCF4\uB97C \uD655\uC778\uD574 \uC8FC\uC138\uC694."),
    LOGIN_ID_NOT_FOUND(HttpStatus.UNAUTHORIZED, "\uB85C\uADF8\uC778 \uC544\uC774\uB514\uB97C \uD655\uC778\uD574 \uC8FC\uC138\uC694."),
    INVALID_LOGIN_PASSWORD(HttpStatus.UNAUTHORIZED, "\uBE44\uBC00\uBC88\uD638\uB97C \uD655\uC778\uD574 \uC8FC\uC138\uC694."),
    GYM_NOT_FOUND(HttpStatus.NOT_FOUND, "\uB3C4\uC7A5 \uC815\uBCF4\uB97C \uCC3E\uC744 \uC218 \uC5C6\uC2B5\uB2C8\uB2E4."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "\uD68C\uC6D0\uC744 \uCC3E\uC744 \uC218 \uC5C6\uC2B5\uB2C8\uB2E4."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "\uC11C\uBC84 \uC624\uB958\uAC00 \uBC1C\uC0DD\uD588\uC2B5\uB2C8\uB2E4.");

    private final HttpStatus status;
    private final String message;
}
