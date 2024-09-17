package com.demo.userprojectmanagement.repository;

import com.demo.userprojectmanagement.model.Project;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
  Page<Project> findAll(Pageable pageable);

  Optional<Project> findByName(String name);
}
