package com.promemory.member.service;

import com.promemory.global.exception.CustomException;
import com.promemory.global.exception.type.ErrorCode;
import com.promemory.member.entity.Member;
import com.promemory.member.repository.MemberRepository;
import com.promemory.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Transactional
    public void updateNickname(Member member, String nickname) {
        member.setNickname(nickname);
        member.setFirst(false);
        memberRepository.save(member);
    }

    @Transactional
    public String updateProfileImg(Member member, MultipartFile image) {
        if (image.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_IMG);
        }
        member.setProfileImg(s3Service.uploadFileForProfile(image, member.getEmail()));
        memberRepository.save(member);

        return member.getProfileImg();
    }

}
