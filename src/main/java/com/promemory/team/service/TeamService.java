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
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final ConnectedTeamRepository connectedTeamRepository;
    private final S3Service s3Service;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public TeamDto createTeam(Member member, String teamName, MultipartFile image) {

        if (teamRepository.existsByName(teamName)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_TEAM_NAME);
        }

        String mainImg = s3Service.uploadFileForTeam(image, member.getEmail());

        Team newTeam = saveTeamEntity(teamName, mainImg);

        joinTeam(member, newTeam);

        List<String> nicknames = getTeamMemberByTeam(newTeam);

        return TeamDto.from(newTeam, nicknames);
    }

    public void leaveTeam(Member member, Long teamId) {
        Team team = findTeamById(teamId);

        ConnectedTeam connectedMember = findByConnectedTeamByMemberAndTeam(member, team);

        team.getConnectedTeam().remove(connectedMember);
        connectedTeamRepository.delete(connectedMember);

        if (team.getConnectedTeam().size() < 1) {
            teamRepository.delete(team);
        }
    }

    public String createCodeForInvite(Member member, Long teamId) {
        Team team = findTeamById(teamId);

        if (!connectedTeamRepository.existsByTeamAndMember(team, member)) {
            throw new CustomException(ErrorCode.YOUR_NOT_MEMBER);
        }

        String inviteCod = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(inviteCod, teamId.toString(), 1, TimeUnit.DAYS);

        return inviteCod;
    }

    public TeamDto joinTeamByInviteCode(Member member, String inviteCode) {
        String teamId = redisTemplate.opsForValue().get(inviteCode);
        Team team = findTeamById(Long.parseLong(Objects.requireNonNull(teamId)));

        if (connectedTeamRepository.existsByTeamAndMember(team, member)) {
            throw new CustomException(ErrorCode.ALREADY_JOINED);
        }
        joinTeam(member, team);

        List<String> nicknames = getTeamMemberByTeam(team);

        return TeamDto.from(team, nicknames);

    }

    private void joinTeam(Member member, Team team) {
        connectedTeamRepository.save(
                ConnectedTeam.builder()
                        .team(team)
                        .member(member)
                        .build()
        );
    }

    private Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
    }


    private ConnectedTeam findByConnectedTeamByMemberAndTeam(Member member, Team team) {
        return connectedTeamRepository.findByTeamAndMember(team, member)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
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
