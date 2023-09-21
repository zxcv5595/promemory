package com.promemory.member.controller;

import com.promemory.global.exception.dto.ErrorResponse;
import com.promemory.member.annotation.CurrentUser;
import com.promemory.member.dto.MemberDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Operation(summary = "닉네임, 프로필 사진 변경",
            description = """
                    image/ multipart/form-data (5MB 미만)
                    data/ application/json ex:{"nickname":"창호"}
                                        
                    닉네임과 프로필 사진을 변경 해줌
                                        
                    프로필 사진 첨부는 선택사항
                                        
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
    @PostMapping
    public ResponseEntity<MemberDto> updateUserDetails(
            @CurrentUser Member member,
            @RequestPart(value = "data") @Valid UpdateNicknameRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {

        Member updatedMember = memberService.updateUserDetails(member, request.getNickname(),
                image);

        return ResponseEntity.ok(MemberDto.from(updatedMember));
    }
}
