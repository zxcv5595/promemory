package com.promemory.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PublishProject {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        private String roomId;
        private String projectName;
        private String intro;
        private Boolean isPublic;
    }

}
