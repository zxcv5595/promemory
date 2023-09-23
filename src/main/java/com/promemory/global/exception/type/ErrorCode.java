package com.promemory.global.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ACCESS_DENIED("접근권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND_USER("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_IMG("이미지 파일이 필요합니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_MEMORY("등록되지 않은 회고록입니다", HttpStatus.NOT_FOUND),
    YOUR_NOT_MEMBER("팀에 속한 구성원이 아닙니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXIST_PROJECT_NAME("이미 존재하는 프로젝트명 입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_JOINED("이미 가입된 팀 입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_INVITE_CODE("만료된 초대코드 입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXIST_MEMORY("이미 회고록이 존재합니다.", HttpStatus.BAD_REQUEST),
    FAILED_UPLOAD("업로드에 실패했습니다. 지원하지않는 파일 일 수 있습니다.", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED("잘못된 요청 값입니다.", HttpStatus.BAD_REQUEST);

    public static final String NICKNAME_NOT_NULL_MESSAGE = "닉네임을 입력해주세요.";
    public static final String TEAM_NAME_NOT_NULL_MESSAGE = "팀명을 입력해주세요.";
    public static final String NOT_NULL_MEMORY_TYPE = "등록된 회고록의 타입값은 필수값입니다. WHITE_BOARD 혹은 FORMAL";
    public static final String NICKNAME_SIZE_MESSAGE = "닉네임은 20자를 넘길 수 없습니다.";
    public static final String TEAM_NAME_SIZE_MESSAGE = "팀명은 20자를 넘길 수 없습니다.";
    public static final String NOT_EMPTY_DOCUMENT = "회고록이 저장된 documentNames 가 필요합니다.";

    private final String message;
    private final HttpStatus status;


}