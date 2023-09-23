package com.promemory.memory.dto;

import com.promemory.memory.entity.ConnectedMembers;
import com.promemory.memory.entity.Project;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProjectListDto {

    private String room_id;
    private String ProjectName;
    private String intro;
    private String mainImg;
    private Long likes;
    private List<String> membersNicknames;

    public static GetProjectListDto from(Project project) {
        List<ConnectedMembers> connectedMembers = project.getMemory().getConnectedMembers();
        List<String> memberNicknames = connectedMembers.stream()
                .map(connectedMember -> connectedMember.getMember().getNickname())
                .toList();

        return GetProjectListDto.builder()
                .room_id(project.getMemory().getRoomId())
                .ProjectName(project.getName())
                .intro(project.getIntro())
                .mainImg(project.getMainImg())
                .membersNicknames(memberNicknames)
                .likes(project.getLikes())
                .build();
    }
}
