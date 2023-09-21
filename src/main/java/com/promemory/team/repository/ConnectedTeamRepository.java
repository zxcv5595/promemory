package com.promemory.team.repository;

import com.promemory.team.entity.ConnectedTeam;
import com.promemory.team.entity.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectedTeamRepository extends JpaRepository<ConnectedTeam, Long> {
    List<ConnectedTeam> findByTeam(Team team);
}
