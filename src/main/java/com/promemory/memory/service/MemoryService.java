package com.promemory.memory.service;

import com.promemory.global.exception.CustomException;
import com.promemory.global.exception.type.ErrorCode;
import com.promemory.member.entity.Member;
import com.promemory.memory.dto.PublishMemory;
import com.promemory.memory.dto.PublishMemory.Request;
import com.promemory.memory.dto.PublishMemory.Response;
import com.promemory.memory.entity.Document;
import com.promemory.memory.entity.Memory;
import com.promemory.memory.repository.DocumentRepository;
import com.promemory.memory.repository.MemoryRepository;
import com.promemory.team.entity.Team;
import com.promemory.team.repository.ConnectedMembersRepository;
import com.promemory.team.repository.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemoryService {

    private final MemoryRepository memoryRepository;
    private final DocumentRepository documentRepository;
    private final TeamRepository teamRepository;
    private final ConnectedMembersRepository connectedMembersRepository;

    @Transactional
    public Response publishMemory(Member member, Request request) {

        Team team = findTeamByTeamName(request.getTeamName());

        if (memoryRepository.existsByTeam(team)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMORY);
        }

        validateMember(member, team);

        List<Document> documents = request.getDocumentNames().stream().map(Document::new)
                .toList();

        documentRepository.saveAll(documents);

        Memory memory = memoryRepository.save(Memory.builder()
                .team(team)
                .memoryType(request.getMemoryType())
                .isPublic(request.getIsPublic())
                .likes(0L)
                .build());

        documents.forEach(memory::addDocument);

        return PublishMemory.Response.builder()
                .teamName(team.getName())
                .mainImg(team.getMainImg())
                .documentNames(request.getDocumentNames())
                .memoryType(memory.getMemoryType())
                .isPublic(memory.isPublic())
                .likes(memory.getLikes())
                .build();
    }

    private void validateMember(Member member, Team team) {
        if (!connectedMembersRepository.existsByTeamAndMember(team, member)) {
            throw new CustomException(ErrorCode.YOUR_NOT_MEMBER);
        }
    }

    private Team findTeamByTeamName(String teamName) {
        return teamRepository.findByName(teamName)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.NOT_FOUND_TEAM));
    }
}
