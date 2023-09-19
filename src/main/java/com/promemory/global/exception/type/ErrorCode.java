package com.promemory.global.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ACCESS_DENIED("접근권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND_USER("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    VALIDATION_FAILED("잘못된 요청 값입니다.",HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus status;


}