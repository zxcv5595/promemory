package com.promemory.memory.controller;

import com.promemory.member.annotation.CurrentUser;
import com.promemory.member.entity.Member;
import com.promemory.memory.dto.GetProjectListDto;
import com.promemory.memory.dto.JoinMemory;
import com.promemory.memory.dto.PublishProject;
import com.promemory.memory.service.MemoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/memory")
public class MemoryController {

    private final MemoryService memoryService;

    @PostMapping
    public ResponseEntity<Void> createMemory(
            @CurrentUser Member member,
            String roomId
    ) {
        memoryService.createMemory(member, roomId);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/project")
    public ResponseEntity<Void> publishProject(
            @CurrentUser Member member,
            @RequestPart(name = "data") PublishProject.Request request,
            @RequestPart(name = "image") MultipartFile image
    ) {
        memoryService.publishProject(member, request, image);

        return ResponseEntity.ok(null);
    }

    @GetMapping
    public String getInviteCode(
            @CurrentUser Member member,
            String roomId
    ) {
        return memoryService.getInviteCode(member, roomId);
    }

    @PostMapping("/join/{code}")
    public JoinMemory.Response joinMemory(
            @CurrentUser Member member,
            @PathVariable(name = "code") String inviteCode
    ) {
        return memoryService.joinMemory(member, inviteCode);
    }

    @GetMapping("/list")
    public List<GetProjectListDto> getMemoryList(){
        return memoryService.getProjectList();
    }
}
