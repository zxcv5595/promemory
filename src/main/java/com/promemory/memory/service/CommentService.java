package com.promemory.memory.service;

import com.promemory.global.exception.CustomException;
import com.promemory.global.exception.type.ErrorCode;
import com.promemory.member.entity.Member;
import com.promemory.memory.dto.CommentDto;
import com.promemory.memory.dto.CreateComment;
import com.promemory.memory.entity.Comment;
import com.promemory.memory.entity.Memory;
import com.promemory.memory.repository.CommentRepository;
import com.promemory.memory.repository.MemoryRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemoryRepository memoryRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createComment(Member member, CreateComment request) {
        Memory memory = findMemoryByRoomId(request.getRoomId());
        commentRepository.save(Comment.builder()
                .comment(request.getComment())
                .member(member)
                .memory(memory)
                .build());
    }

    @Transactional
    public List<CommentDto> getCommentByRoomId(String roomId) {
        Memory memory = findMemoryByRoomId(roomId);
        List<Comment> comments = memory.getComments();
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .map(CommentDto::from)
                .toList();

    }

    private Memory findMemoryByRoomId(String roomId) {
        return memoryRepository.findByRoomId(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMORY));
    }
}
