package com.promemory.memory.controller;

import com.promemory.member.annotation.CurrentUser;
import com.promemory.member.entity.Member;
import com.promemory.memory.dto.PublishMemory;
import com.promemory.memory.service.MemoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/memory")
public class MemoryController {

    private final MemoryService memoryService;

    @PostMapping
    public PublishMemory.Response publishMemory(
            @CurrentUser Member member,
            @Valid @RequestBody PublishMemory.Request request
    ) {
        return memoryService.publishMemory(member, request);
    }
}
