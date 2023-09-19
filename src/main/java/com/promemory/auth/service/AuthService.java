package com.promemory.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promemory.auth.dto.KakaoLoginResponse;
import com.promemory.auth.openfeign.KakaoApiClient;
import com.promemory.auth.openfeign.KakaoAuthClient;
import com.promemory.auth.openfeign.dto.KakaoProfile;
import com.promemory.auth.openfeign.dto.KakaoTokenResponse;
import com.promemory.member.entity.Member;
import com.promemory.member.repository.MemberRepository;
import com.promemory.member.type.Role;
import com.promemory.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    @Value("${auth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${auth.kakao.redirectUri}")
    private String kakaoRedirectUri;

    @Value("${auth.kakao.grantType}")
    private String kakaoGrantType;

    public String getKakaoToken(String code) throws JsonProcessingException {
        ResponseEntity<String> response = kakaoAuthClient.getToken(kakaoClientId, kakaoRedirectUri,
                kakaoGrantType, code);

        KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(response.getBody(),
                KakaoTokenResponse.class);

        return kakaoTokenResponse.getAccess_token();
    }

    public KakaoProfile getKakaoProfile(String accessToken)
            throws JsonProcessingException {
        String authorization = "Bearer " + accessToken;
        ResponseEntity<String> response = kakaoApiClient.getUserInfo(authorization);

        return objectMapper.readValue(response.getBody(), KakaoProfile.class);
    }

    public KakaoLoginResponse kakaoLogin(KakaoProfile kakaoProfile) {
        String memberEmail = kakaoProfile.kakao_account.email;

        Member member = memberRepository.findByEmail(memberEmail)
                .orElseGet(() -> kakaoSignUp(kakaoProfile));

        String token = tokenProvider.generateToken(member.getEmail(), member.getId(), member.getRole());

        return KakaoLoginResponse.from(member, token);
    }

    public Member kakaoSignUp(KakaoProfile kakaoProfile) {
        Member member = Member.builder()
                .email(kakaoProfile.kakao_account.email)
                .nickname(kakaoProfile.properties.nickname)
                .profileImg(kakaoProfile.properties.profile_image)
                .role(Role.ROLE_USER)
                .isFirst(true)
                .build();

        return memberRepository.save(member);
    }

}
