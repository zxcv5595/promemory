package com.promemory.team.dto;

import com.promemory.team.entity.Team;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDto {

    private Long id;
    private String teamName;
    private String mainImg;
    private List<String> nicknamesOfMembers;

    public static TeamDto from(Team team, List<String> nicknames){
        return TeamDto.builder()
                .id(team.getId())
                .teamName(team.getName())
                .mainImg(team.getMainImg())
                .nicknamesOfMembers(nicknames)
                .build();
    }
}
