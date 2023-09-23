package com.promemory.memory.repository;

import com.promemory.memory.entity.Memory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory,Long> {
    Optional<Memory> findByRoomId(String roomId);
    boolean existsByRoomId(String roomId);
}
