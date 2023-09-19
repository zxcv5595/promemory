package com.promemory.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.promemory.auth.openfeign.dto.KakaoProfile;
import com.promemory.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@Parameter String code) throws JsonProcessingException {

        String kakaoToken = authService.getKakaoToken(code);
        KakaoProfile kakaoProfile = authService.getKakaoProfile(kakaoToken);
        return authService.kakaoLogin(kakaoProfile);
    }
}
