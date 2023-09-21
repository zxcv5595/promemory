package com.promemory.team.service;

import com.promemory.global.exception.CustomException;
import com.promemory.global.exception.type.ErrorCode;
import com.promemory.member.entity.Member;
import com.promemory.s3.service.S3Service;
import com.promemory.team.dto.TeamDto;
import com.promemory.team.entity.ConnectedTeam;
import com.promemory.team.entity.Team;
import com.promemory.team.repository.ConnectedTeamRepository;
import com.promemory.team.repository.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final ConnectedTeamRepository connectedTeamRepository;
    private final S3Service s3Service;

    @Transactional
    public TeamDto createTeam(Member member, String teamName, MultipartFile image) {

        if (teamRepository.existsByName(teamName)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_TEAM_NAME);
        }

        String mainImg = s3Service.uploadFileForTeam(image, member.getEmail());

        Team newTeam = saveTeamEntity(teamName, mainImg);

        ConnectedTeam connectedTeam = connectedTeamRepository.save(
                ConnectedTeam.builder()
                        .team(newTeam)
                        .member(member)
                        .build()
        );
        newTeam.getConnectedTeam().add(connectedTeam);

        List<String> nicknames = getTeamMemberByTeam(newTeam);

        return TeamDto.from(newTeam, nicknames);
    }

    public void leaveTeam(Member member, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));


        ConnectedTeam connectedMember = connectedTeamRepository.findByTeamAndMember(team, member)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        team.getConnectedTeam().remove(connectedMember);
        connectedTeamRepository.delete(connectedMember);

        if(team.getConnectedTeam().size()<=1){
            teamRepository.delete(team);
        }
    }


    private List<String> getTeamMemberByTeam(Team team) {
        List<ConnectedTeam> connectedMembers = connectedTeamRepository.findByTeam(team);
        connectedMembers.get(0).getMember().getNickname();

        return connectedMembers.stream()
                .map(connectedMember -> connectedMember.getMember().getNickname()).toList();
    }

    private Team saveTeamEntity(String teamName, String mainImg) {
        return teamRepository.save(new Team(teamName, mainImg));
    }


}
