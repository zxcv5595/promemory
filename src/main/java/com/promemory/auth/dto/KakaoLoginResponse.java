package com.promemory.auth.dto;

import com.promemory.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoLoginResponse {

    private String nickname;
    private boolean isFirst;
    private String jwt;

    public static KakaoLoginResponse from(Member member, String jwt) {
        return KakaoLoginResponse.builder()
                .nickname(member.getNickname())
                .isFirst(member.isFirst())
                .jwt(jwt)
                .build();
    }

}
