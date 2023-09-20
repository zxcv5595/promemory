package com.promemory.member.controller;

import com.promemory.global.exception.dto.ErrorResponse;
import com.promemory.member.annotation.CurrentUser;
import com.promemory.member.dto.UpdateNicknameRequest;
import com.promemory.member.entity.Member;
import com.promemory.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/member")
@Tag(name = "Member API", description = "회원정보 수정과 같은 회원에 관련된 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "닉네임 변경",
            description = """
                    json 타입으로 닉네임을 필요로함 {"nickname"=""}
                                        
                    닉네임을 변경 해줌
                                        
                    첫 회원 여부를 false로 변경함
                    
                    인증을 위해 Bearer {json web token} 을 헤더 Authorization 에 첨부해야 함
                                        
                    403, 401, 200값을 응답함
                    
                    200:정상
                    
                    403: 토큰값이 없거나 Bearer 형식에 맞지 않음
                    
                    401: 만료, 혹은 변조된 토큰
                    
                    """, responses = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
            @ApiResponse(responseCode = "403", description = "토큰값이 없거나 Bearer 형식에 맞지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "만료, 혹은 변조된 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateNickname(@CurrentUser Member member,
            @Valid @RequestBody UpdateNicknameRequest request) {

        memberService.updateNickname(member, request.getNickname());

        return ResponseEntity.ok(null);
    }
}
