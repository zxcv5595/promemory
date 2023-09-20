package com.promemory.global.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ACCESS_DENIED("접근권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND_USER("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    VALIDATION_FAILED("잘못된 요청 값입니다.", HttpStatus.BAD_REQUEST);

    public static final String NICKNAME_NOT_NULL_MESSAGE = "닉네임을 입력해주세요.";
    public static final String NICKNAME_SIZE_MESSAGE = "닉네임은 20자를 넘길 수 없습니다.";

    private final String message;
    private final HttpStatus status;


}