package com.promemory.team.controller;

import com.promemory.member.annotation.CurrentUser;
import com.promemory.member.entity.Member;
import com.promemory.team.dto.CreateTeamRequest;
import com.promemory.team.dto.TeamDto;
import com.promemory.team.entity.Team;
import com.promemory.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/team")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(
            @CurrentUser Member member,
            @RequestPart(value = "image") MultipartFile image,
            @RequestPart(value = "data") CreateTeamRequest request
            ){
        TeamDto teamDto = teamService.createTeam(member, request.getTeamName(), image);
        return ResponseEntity.ok(teamDto);
    }
}