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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Void> updateNickname(
            @CurrentUser Member member,
            @Valid @RequestBody UpdateNicknameRequest request
    ) {

        memberService.updateNickname(member, request.getNickname());

        return ResponseEntity.ok(null);
    }

    @Operation(summary = "프로필 사진 변경",
            description = """
                    multipart/form-data 형식의 이미지파일과 jwt 토큰 필요함
                                        
                    이미지 파일 용량 5MB로 제한
                                        
                    프로필을 변경 해줌 변경된 
                    
                    img url과 200을 응답함, json 아니고 String 으로 응답                       
                                        
                    인증을 위해 Bearer {json web token} 을 헤더 Authorization 에 첨부해야 함
                                        
                    415, 403, 401, 200값을 응답함
                                        
                    200:정상
                                        
                    415: 이미지 첨부하지 않았을 때
                                        
                    403: 토큰값이 없거나 Bearer 형식에 맞지 않음
                                        
                    401: 만료, 혹은 변조된 토큰
                                        
                    """, responses = {
            @ApiResponse(responseCode = "200", description = "프로필 변경 성공 및 url 반환 (String)"),
            @ApiResponse(responseCode = "415", description = "이미지를 첨부하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "토큰값이 없거나 Bearer 형식에 맞지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "만료, 혹은 변조된 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProfileImg(
            @CurrentUser Member member,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        String profileUrl = memberService.updateProfileImg(member, image);

        return ResponseEntity.ok(profileUrl);
    }
}
