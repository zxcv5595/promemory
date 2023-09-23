package com.promemory.memory.repository;

import com.promemory.memory.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {

}
