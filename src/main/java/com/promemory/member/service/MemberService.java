package com.promemory.member.service;

import com.promemory.member.entity.Member;
import com.promemory.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateNickname(Member member, String nickname) {
        member.setNickname(nickname);
        member.setFirst(false);
        memberRepository.save(member);
    }

}
