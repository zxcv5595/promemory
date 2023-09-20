package com.promemory.member.controller;

import com.promemory.member.annotation.CurrentUser;
import com.promemory.member.dto.UpdateNicknameRequest;
import com.promemory.member.entity.Member;
import com.promemory.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> updateNickname(@CurrentUser Member member,
            @Valid @RequestBody UpdateNicknameRequest request) {

        memberService.updateNickname(member, request.getNickname());

        return ResponseEntity.ok(null);
    }
}
