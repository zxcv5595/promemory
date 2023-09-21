package com.promemory.member.dto;

import com.promemory.member.entity.Member;
import com.promemory.member.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String nickname;
    private String email;
    private String profileImg;
    private Role role;
    private boolean isFirst;

    public static MemberDto from(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg())
                .isFirst(member.isFirst())
                .role(member.getRole())
                .build();
    }
}
