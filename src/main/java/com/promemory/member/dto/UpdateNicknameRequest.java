package com.promemory.member.dto;

import static com.promemory.global.exception.type.ErrorCode.NICKNAME_NOT_NULL_MESSAGE;
import static com.promemory.global.exception.type.ErrorCode.NICKNAME_SIZE_MESSAGE;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNicknameRequest {

    @NotEmpty(message = NICKNAME_NOT_NULL_MESSAGE)
    @Size(max = 20, message = NICKNAME_SIZE_MESSAGE)
    private String nickname;

}
