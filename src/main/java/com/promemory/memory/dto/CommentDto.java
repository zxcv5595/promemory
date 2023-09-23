package com.promemory.memory.dto;

import com.promemory.memory.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private String nickname;
    private String comment;
    private LocalDateTime createdAt;

    public static CommentDto from(Comment comment){
        return CommentDto.builder()
                .nickname(comment.getMember().getNickname())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
