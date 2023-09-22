package com.promemory.memory.dto;

import static com.promemory.global.exception.type.ErrorCode.NOT_EMPTY_DOCUMENT;
import static com.promemory.global.exception.type.ErrorCode.NOT_NULL_MEMORY_TYPE;

import com.promemory.memory.type.MemoryType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PublishMemory {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String teamName;
        private boolean isPublic;
        @NotEmpty(message = NOT_EMPTY_DOCUMENT)
        private List<String> documentNames;

        @NotNull(message = NOT_NULL_MEMORY_TYPE)
        @Enumerated(EnumType.STRING)
        private MemoryType memoryType;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private String teamName;
        private String mainImg;
        private List<String> documentNames;
        private boolean isPublic;
        private Long likes;

        @Enumerated(EnumType.STRING)
        private MemoryType memoryType;

    }


}
