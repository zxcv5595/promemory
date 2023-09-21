package com.promemory.auth.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-api", url = "https://kapi.kakao.com")
public interface KakaoApiClient {

    @PostMapping(value = "/v2/user/me", produces = "application/x-www-form-urlencoded;charset=utf-8")
    ResponseEntity<String> getUserInfo(
            @RequestHeader("Authorization") String authorization
    );
}
