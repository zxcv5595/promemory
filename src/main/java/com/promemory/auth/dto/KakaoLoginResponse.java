package com.promemory.auth.dto;

import com.promemory.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "카카오 로그인 응답 DTO")
public class KakaoLoginResponse {
    @Schema(description = "memberId", defaultValue = "1")
    private Long memberId;

    @Schema(description = "카카오 닉네임", defaultValue = "이창호")
    private String nickname;

    @Schema(description = "카카오 프로필 사진", defaultValue = "사진 URL")
    private String profileImg;

    @Schema(description = "첫 회원 여부 (true - 첫 회원)", defaultValue = "true", allowableValues = {"true", "false"})
    private boolean isFirst;

    @Schema(description = "jwt 토큰 값", defaultValue = "jwt 토큰값")
    private String jwt;

    public static KakaoLoginResponse from(Member member, String jwt) {
        return KakaoLoginResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg())
                .isFirst(member.isFirst())
                .jwt(jwt)
                .build();
    }

}
