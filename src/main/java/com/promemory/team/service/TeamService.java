package com.promemory.team.service;

import com.promemory.global.exception.CustomException;
import com.promemory.global.exception.type.ErrorCode;
import com.promemory.member.entity.Member;
import com.promemory.s3.service.S3Service;
import com.promemory.team.dto.TeamDto;
import com.promemory.team.entity.ConnectedMembers;
import com.promemory.team.entity.Team;
import com.promemory.team.repository.ConnectedMembersRepository;
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
    private final ConnectedMembersRepository connectedMembersRepository;
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

        ConnectedMembers connectedMember = findByConnectedTeamByMemberAndTeam(member, team);

        team.getConnectedMembers().remove(connectedMember);
        connectedMembersRepository.delete(connectedMember);

        if (team.getConnectedMembers().size() < 1) {
            teamRepository.delete(team);
        }
    }

    public String createCodeForInvite(Member member, Long teamId) {
        Team team = findTeamById(teamId);

        if (!connectedMembersRepository.existsByTeamAndMember(team, member)) {
            throw new CustomException(ErrorCode.YOUR_NOT_MEMBER);
        }

        String inviteCod = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(inviteCod, teamId.toString(), 1, TimeUnit.DAYS);

        return inviteCod;
    }

    public TeamDto joinTeamByInviteCode(Member member, String inviteCode) {
        String teamId = redisTemplate.opsForValue().get(inviteCode);
        Team team = findTeamById(Long.parseLong(Objects.requireNonNull(teamId)));

        if (connectedMembersRepository.existsByTeamAndMember(team, member)) {
            throw new CustomException(ErrorCode.ALREADY_JOINED);
        }
        joinTeam(member, team);

        List<String> nicknames = getTeamMemberByTeam(team);

        return TeamDto.from(team, nicknames);

    }

    private void joinTeam(Member member, Team team) {
        connectedMembersRepository.save(
                ConnectedMembers.builder()
                        .team(team)
                        .member(member)
                        .build()
        );
    }

    private Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
    }


    private ConnectedMembers findByConnectedTeamByMemberAndTeam(Member member, Team team) {
        return connectedMembersRepository.findByTeamAndMember(team, member)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    private List<String> getTeamMemberByTeam(Team team) {
        List<ConnectedMembers> connectedMembers = connectedMembersRepository.findByTeam(team);
        connectedMembers.get(0).getMember().getNickname();

        return connectedMembers.stream()
                .map(connectedMember -> connectedMember.getMember().getNickname()).toList();
    }

    private Team saveTeamEntity(String teamName, String mainImg) {
        return teamRepository.save(new Team(teamName, mainImg));
    }


}
