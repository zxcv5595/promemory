package com.promemory.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.promemory.auth.dto.KakaoLoginResponse;
import com.promemory.auth.service.AuthService;
import com.promemory.global.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Kakao 로그인 및 회원가입", description = "카카오 로그인 요청주소로 연결하면, 로그인 및 회원가입이 진행됩니다. [slack] DM으로 파일 첨부")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인",
            description = """
                    카카오로부터 code와 access token을 받아 사용자 정보를 받아오고, 이 정보로 로그인 또는 회원가입 진행

                    jwt값, 닉네임, 프로필 사진 그리고 첫 회원 여부를 리턴해주며, 회원전용 서비스를 사용할 때 사용됨

                    회원이 아닌 유저는 회원가입이 자동으로 완료 되고 jwt값을 리턴해줌
                                        
                    first 값이 true이면 첫 회원임을 의미함

                    """, responses = {
            @ApiResponse(responseCode = "200", description = "로그인 및 회원가입 성공", content = @Content(schema = @Schema(implementation = KakaoLoginResponse.class))),
            @ApiResponse(responseCode = "403", description = "email 제공 거부 시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @GetMapping(value = "/auth/kakao/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KakaoLoginResponse> kakaoCallback(@RequestParam String code)
            throws JsonProcessingException {

        KakaoLoginResponse kakaoLoginResponse = authService.kakaoLogin(code);

        return ResponseEntity.ok(kakaoLoginResponse);
    }
}
