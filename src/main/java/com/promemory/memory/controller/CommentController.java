package com.promemory.memory.controller;

import com.promemory.member.annotation.CurrentUser;
import com.promemory.member.entity.Member;
import com.promemory.memory.dto.CommentDto;
import com.promemory.memory.dto.CreateComment;
import com.promemory.memory.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
            @CurrentUser Member member,
            @RequestBody CreateComment request
    ) {
        commentService.createComment(member, request);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CommentDto>> getCommentByRoomId(
            String roomId
    ) {
        List<CommentDto> comments = commentService.getCommentByRoomId(roomId);

        return ResponseEntity.ok(comments);
    }

}
