package com.promemory.auth.openfeign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoTokenResponse {

    private String access_token;
    private String token_type;
    private String expires_in;
    private String scope;
    private String refresh_token_expires_in;

}
