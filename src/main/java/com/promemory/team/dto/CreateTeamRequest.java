package com.promemory.team.dto;

import static com.promemory.global.exception.type.ErrorCode.NICKNAME_NOT_NULL_MESSAGE;
import static com.promemory.global.exception.type.ErrorCode.TEAM_NAME_NOT_NULL_MESSAGE;
import static com.promemory.global.exception.type.ErrorCode.TEAM_NAME_SIZE_MESSAGE;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class CreateTeamRequest {

    @NotEmpty(message = TEAM_NAME_NOT_NULL_MESSAGE)
    @Size(max = 20, message = TEAM_NAME_SIZE_MESSAGE)
    private String teamName;
}
