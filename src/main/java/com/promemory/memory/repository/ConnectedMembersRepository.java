package com.promemory.memory.repository;

import com.promemory.memory.entity.ConnectedMembers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectedMembersRepository extends JpaRepository<ConnectedMembers,Long> {

}
