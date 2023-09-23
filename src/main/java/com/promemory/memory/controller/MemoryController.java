package com.promemory.memory.controller;

import com.promemory.member.annotation.CurrentUser;
import com.promemory.member.entity.Member;
import com.promemory.memory.dto.GetProjectListDto;
import com.promemory.memory.dto.JoinMemory;
import com.promemory.memory.dto.JoinMemory.Response;
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
    public ResponseEntity<String> getInviteCode(
            @CurrentUser Member member,
            String roomId
    ) {
        String inviteCode = memoryService.getInviteCode(member, roomId);

        return ResponseEntity.ok(inviteCode);
    }

    @PostMapping("/join/{code}")
    public ResponseEntity<JoinMemory.Response> joinMemory(
            @CurrentUser Member member,
            @PathVariable(name = "code") String inviteCode
    ) {
        Response response = memoryService.joinMemory(member, inviteCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<GetProjectListDto>> getProjectList() {
        List<GetProjectListDto> projectList = memoryService.getProjectList();
        return ResponseEntity.ok(projectList);
    }

    @GetMapping("/member/list")
    public ResponseEntity<List<GetProjectListDto>> getProjectListByMember(
            @CurrentUser Member member
    ) {
        List<GetProjectListDto> projectList = memoryService.getProjectListByMember(member);
        return ResponseEntity.ok(projectList);
    }

    @PostMapping("/auth")
    public ResponseEntity<Boolean> checkAuthForUpdateMemory(
            @CurrentUser Member member,
            String roomId
    ) {
        Boolean auth = memoryService.checkAuthForUpdateMemory(member, roomId);
        return ResponseEntity.ok(auth);
    }
}
