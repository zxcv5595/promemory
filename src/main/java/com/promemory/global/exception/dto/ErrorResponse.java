package com.promemory.global.exception.dto;


import com.promemory.global.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final ErrorCode errorCode;
    private final int status;
    private final String message;

}
