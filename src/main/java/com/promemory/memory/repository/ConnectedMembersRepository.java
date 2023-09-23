package com.promemory.memory.repository;

import com.promemory.member.entity.Member;
import com.promemory.memory.entity.ConnectedMembers;
import com.promemory.memory.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectedMembersRepository extends JpaRepository<ConnectedMembers,Long> {
    boolean existsByMemoryAndMember(Memory memory, Member member);
}
