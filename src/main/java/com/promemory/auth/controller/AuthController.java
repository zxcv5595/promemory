package com.promemory.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.promemory.auth.openfeign.dto.KakaoProfile;
import com.promemory.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인",
            description = """
                    카카오로부터 code와 access token을 받아 사용자 정보를 받아오고, 이 정보로 로그인 또는 회원가입 진행

                    jwt값을 리턴해주며, 회원전용 서비스를 사용할 때 사용됨

                    회원이 아닌 유저는 회원가입되고 jwt값을 리턴해줌

                    """)
    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@Parameter String code) throws JsonProcessingException {

        String kakaoToken = authService.getKakaoToken(code);
        KakaoProfile kakaoProfile = authService.getKakaoProfile(kakaoToken);
        return authService.kakaoLogin(kakaoProfile);
    }
}
