package com.promemory.auth.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao-auth", url = "https://kauth.kakao.com")
public interface KakaoAuthClient {

    @PostMapping(value = "/oauth/token", produces = "application/x-www-form-urlencoded;charset=utf-8")
    ResponseEntity<String> getToken(@RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String redirectUri,
            @RequestParam("grant_type") String grantType,
            @RequestParam("code") String code);
}