package com.promemory.memory.repository;

import com.promemory.memory.entity.Memory;
import com.promemory.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    boolean existsByTeam(Team team);
}
