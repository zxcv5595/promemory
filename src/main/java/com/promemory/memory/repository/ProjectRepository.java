package com.promemory.memory.repository;

import com.promemory.memory.entity.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    boolean existsByName(String projectName);
    List<Project> findAllByPublicFieldIsTrueOrderByLikesDesc();

}
