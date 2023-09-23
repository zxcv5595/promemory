package com.promemory.memory.service;

import com.promemory.global.exception.CustomException;
import com.promemory.global.exception.type.ErrorCode;
import com.promemory.member.entity.Member;
import com.promemory.memory.dto.GetProjectListDto;
import com.promemory.memory.dto.JoinMemory;
import com.promemory.memory.dto.JoinMemory.Response;
import com.promemory.memory.dto.PublishProject;
import com.promemory.memory.entity.ConnectedMembers;
import com.promemory.memory.entity.Memory;
import com.promemory.memory.entity.Project;
import com.promemory.memory.repository.ConnectedMembersRepository;
import com.promemory.memory.repository.MemoryRepository;
import com.promemory.memory.repository.ProjectRepository;
import com.promemory.s3.service.S3Service;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemoryService {

    private final ConnectedMembersRepository connectedMembersRepository;
    private final MemoryRepository memoryRepository;
    private final ProjectRepository projectRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final S3Service s3Service;

    @Transactional
    public void createMemory(Member member, String roomId) {

        if (memoryRepository.existsByRoomId(roomId)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMORY);
        }

        Memory memory = memoryRepository.save(new Memory(roomId));

        connectedMembersRepository.save(
                ConnectedMembers.builder()
                        .member(member)
                        .memory(memory)
                        .build()
        );
    }

    @Transactional
    public void publishProject(
            Member member,
            PublishProject.Request request,
            MultipartFile image
    ) {
        Memory memory = findMemoryByRoomId(request.getRoomId());

        if(!connectedMembersRepository.existsByMemoryAndMember(memory,member)){
            throw new CustomException(ErrorCode.YOUR_NOT_MEMBER);
        }

        String mainImg = s3Service.uploadFileForProject(image, request.getRoomId());

        projectRepository.save(
                Project.builder()
                        .name(request.getProjectName())
                        .intro(request.getIntro())
                        .publicField(request.getIsPublic())
                        .mainImg(mainImg)
                        .memory(memory)
                        .likes(0L)
                        .build()
        );
    }

    @Transactional
    public String getInviteCode(Member member, String roomId) {
        Memory memory = findMemoryByRoomId(roomId);

        if (!connectedMembersRepository.existsByMemoryAndMember(memory, member)) {
            throw new CustomException(ErrorCode.YOUR_NOT_MEMBER);
        }

        String inviteCode = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(inviteCode, roomId, 1, TimeUnit.HOURS);

        return inviteCode;
    }

    @Transactional
    public Response joinMemory(Member member, String inviteCode) {
        String roomId = redisTemplate.opsForValue().get(inviteCode);

        if (roomId == null) {
            throw new CustomException(ErrorCode.EXPIRED_INVITE_CODE);
        }

        Memory memory = findMemoryByRoomId(roomId);

        if (connectedMembersRepository.existsByMemoryAndMember(memory, member)) {
            throw new CustomException(ErrorCode.ALREADY_JOINED);
        }
        connectedMembersRepository.save(
                ConnectedMembers.builder()
                        .memory(memory)
                        .member(member)
                        .build()
        );

        List<ConnectedMembers> connectedMembers = connectedMembersRepository.findByMemory(memory);

        List<String> memberNicknames = connectedMembers.stream()
                .map(connectedMember -> connectedMember.getMember().getNickname())
                .toList();

        return JoinMemory.Response.builder()
                .room_id(roomId)
                .memberNicknames(memberNicknames)
                .build();
    }

    @Transactional
    public List<GetProjectListDto> getProjectList() {
        List<Project> projects = projectRepository.findAllByPublicFieldIsTrueOrderByLikesDesc();

        return projects.stream().map(GetProjectListDto::from).toList();

    }

    @Transactional
    public List<GetProjectListDto> getProjectListByMember(Member member) {
        List<ConnectedMembers> connectedMembers = connectedMembersRepository.findByMemberOrderByCreatedAtDesc(member);
        List<Project> projects = connectedMembers.stream()
                .map(connectedMember -> connectedMember.getMemory().getProject())
                .toList();

        return projects.stream().map(GetProjectListDto::from).toList();
    }

    @Transactional
    public Boolean checkAuthForUpdateMemory(Member member,String roomId){
        Memory memory = findMemoryByRoomId(roomId);
        return connectedMembersRepository.existsByMemoryAndMember(memory, member);
    }

    private Memory findMemoryByRoomId(String roomId) {
        return memoryRepository.findByRoomId(roomId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.NOT_FOUND_MEMORY));
    }


}
