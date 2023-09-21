package com.promemory.team.repository;

import com.promemory.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByName(String teamName);
}
