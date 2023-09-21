package com.promemory.team.controller;

import com.promemory.global.exception.dto.ErrorResponse;
import com.promemory.member.annotation.CurrentUser;
import com.promemory.member.entity.Member;
import com.promemory.team.dto.CreateTeamRequest;
import com.promemory.team.dto.TeamDto;
import com.promemory.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/team")
@Tag(name = "Team API", description = "팀 생성, 참여, 나가기와 같은 팀에 관련된 API")
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 생성",
            description = """
                    image:  (5MB 미만)
                                        
                    data: application/json ex:{"teamName":"promemory"}
                                        
                    팀 썸네일 이미지, 팀명 모두 필수 값
                                        
                    팀명은 중복될 수 없음(unique value)
                                        
                    이미 팀 이름이 존재한다면 400 에러
                                        
                    인증을 위해 Bearer {json web token} 을 헤더 Authorization 에 첨부해야 함
                                        
                    403, 401, 200값을 응답함
                                        
                    200:정상
                                        
                    403: 토큰값이 없거나 Bearer 형식에 맞지 않음
                                        
                    401: 만료, 혹은 변조된 토큰
                                        
                    """, responses = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
            @ApiResponse(responseCode = "403", description = "토큰값이 없거나 Bearer 형식에 맞지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "만료, 혹은 변조된 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀 이름이 이미 존재함", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<TeamDto> createTeam(
            @CurrentUser Member member,
            @RequestPart(value = "image") MultipartFile image,
            @Valid @RequestPart(value = "data") CreateTeamRequest request
    ) {
        TeamDto teamDto = teamService.createTeam(member, request.getTeamName(), image);
        return ResponseEntity.ok(teamDto);
    }


    @Operation(summary = "팀 나가기",
            description = """
                    팀을 나감
                                        
                    마지막 인원이 나가면 팀이 삭제됨
                                        
                    200을 응답함
                                        
                    인증을 위해 Bearer {json web token} 을 헤더 Authorization 에 첨부해야 함
                    """, responses = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
            @ApiResponse(responseCode = "403", description = "토큰값이 없거나 Bearer 형식에 맞지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "만료, 혹은 변조된 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 팀이 존재하지 않을 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{teamId}")
    public ResponseEntity<Void> leaveTeam(
            @CurrentUser Member member,
            @PathVariable Long teamId
    ) {
        teamService.leaveTeam(member, teamId);

        return ResponseEntity.ok(null);
    }

    @Operation(summary = "팀 초대코드 생성",
            description = """
                    팀 초대 코드를 생성함
                                        
                    200: 초대코드를 반환함 (String)
                                        
                    마지막 인원이 나가면 팀이 삭제됨
                                        
                    팀에 속한 구성원 만, 초대 가능
                                        
                    인증을 위해 Bearer {json web token} 을 헤더 Authorization 에 첨부해야 함
                    """, responses = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
            @ApiResponse(responseCode = "403", description = "토큰값이 없거나 Bearer 형식에 맞지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "만료, 혹은 변조된 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀에 속한 구성원 만, 초대 가능", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{teamId}")
    public String createCodeForInvite(
            @CurrentUser Member member,
            @PathVariable Long teamId
    ) {
        return teamService.createCodeForInvite(member, teamId);
    }


    @Operation(summary = "팀 참여",
            description = """
                    초대 코드를 통해 팀에 참여함
                                        
                    로그인 되어 있어야함
                                        
                    이미 참여 된 유저는 400에러
                                        
                    인증을 위해 Bearer {json web token} 을 헤더 Authorization 에 첨부해야 함
                    """, responses = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
            @ApiResponse(responseCode = "403", description = "토큰값이 없거나 Bearer 형식에 맞지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "만료, 혹은 변조된 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 참여 된 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/join/{code}")
    public TeamDto joinTeamByInviteCode(
            @CurrentUser Member member,
            @PathVariable(name = "code") String inviteCode
    ) {
        System.out.println(inviteCode);
        return teamService.joinTeamByInviteCode(member, inviteCode);
    }
}
