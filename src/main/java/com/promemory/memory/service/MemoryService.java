package com.promemory.memory.service;

import com.promemory.global.exception.CustomException;
import com.promemory.global.exception.type.ErrorCode;
import com.promemory.member.entity.Member;
import com.promemory.memory.dto.PublishProject;
import com.promemory.memory.entity.ConnectedMembers;
import com.promemory.memory.entity.Memory;
import com.promemory.memory.entity.Project;
import com.promemory.memory.repository.ConnectedMembersRepository;
import com.promemory.memory.repository.MemoryRepository;
import com.promemory.memory.repository.ProjectRepository;
import com.promemory.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemoryService {

    private final ConnectedMembersRepository connectedMembersRepository;
    private final MemoryRepository memoryRepository;
    private final ProjectRepository projectRepository;
    private final S3Service s3Service;

    public void createMemory(Member member, String roomId) {

        if(memoryRepository.existsByRoomId(roomId)){
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

    public void publishProject(
            Member member,
            PublishProject.Request request,
            MultipartFile image
    ) {
        Memory memory = memoryRepository.findByRoomId(request.getRoomId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.NOT_FOUND_MEMORY));

        if(projectRepository.existsByName(request.getProjectName())){
            throw new CustomException(ErrorCode.ALREADY_EXIST_PROJECT_NAME);
        }

        String mainImg = s3Service.uploadFileForProject(image, member.getEmail());

        projectRepository.save(
                Project.builder()
                        .name(request.getProjectName())
                        .intro(request.getIntro())
                        .isPublic(request.getIsPublic())
                        .mainImg(mainImg)
                        .memory(memory)
                        .build()
        );
    }
}
