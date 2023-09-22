package com.promemory.team.repository;

import com.promemory.member.entity.Member;
import com.promemory.team.entity.ConnectedMembers;
import com.promemory.team.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectedMembersRepository extends JpaRepository<ConnectedMembers, Long> {

    List<ConnectedMembers> findByTeam(Team team);

    Optional<ConnectedMembers> findByTeamAndMember(Team team, Member member);

    boolean existsByTeamAndMember(Team team, Member member);
}
