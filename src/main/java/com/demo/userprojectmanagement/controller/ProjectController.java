package com.demo.userprojectmanagement.controller;

import static com.demo.userprojectmanagement.util.Util.convertToResponse;

import com.demo.userprojectmanagement.dto.ProjectDTO;
import com.demo.userprojectmanagement.model.Project;
import com.demo.userprojectmanagement.service.ProjectService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/v1/projects")
public class ProjectController {
  private final ProjectService service;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.service = projectService;
  }

  @GetMapping
  public ResponseEntity<?> findAll(Pageable pageable) {
    Page<ProjectDTO> projects = service.findAll(pageable);

    log.info(String.format("Project list found. Response status: %s", HttpStatus.OK));

    return ResponseEntity.ok(convertToResponse(projects, "projects"));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable long id) {
    Project project = service.findById(id);

    log.info(String.format("Project id=%s found. Response status: %s", id, HttpStatus.OK));

    return ResponseEntity.ok(project);
  }

  @GetMapping(params = "name")
  public ResponseEntity<?> findByName(@RequestParam(required = false) String name) {
    ProjectDTO project = service.findByName(name);

    log.info(String.format("Project name=%s found. Response status: %s", name, HttpStatus.OK));

    return ResponseEntity.ok(project);
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody Project project) {
    var newProject = service.create(project);

    log.info(
        String.format(
            "%s created. Response status: %s", newProject.toString(), HttpStatus.CREATED));

    return ResponseEntity.status(HttpStatus.CREATED).body(newProject);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@RequestBody Project project, @PathVariable long id) {
    var updatedProject = service.update(project, id);

    log.info(
        String.format(
            "Updated: %s. Response status: %s", updatedProject.toString(), HttpStatus.OK));

    return ResponseEntity.ok(updatedProject);
  }

  @PutMapping(
      "/{projectId}/users/assign") // TODO add a single put mapping for both assign/unassign use
  // cases
  public ResponseEntity<?> assignUsersToProject(
      @PathVariable long projectId, @RequestBody List<Long> userIds) {
    var project = service.assignUsersToProject(projectId, userIds);

    log.info(
        String.format(
            "Users assigned to project: %s. Response status: %s",
            project.toString(), HttpStatus.OK));

    return ResponseEntity.ok(project);
  }

  @PutMapping("/{id}/users/unassign")
  public ResponseEntity<?> unassignUsers(@PathVariable long id, @RequestBody List<Long> userIds) {
    var project = service.unassignUsersFromProject(id, userIds);

    log.info(
        String.format(
            "Users unassigned from project: %s. Response status: %s",
            project.toString(), HttpStatus.OK));

    return ResponseEntity.ok(project);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable long id) {
    service.delete(id);

    log.info(String.format("Project with id=%s deleted. Response status: %s", id, HttpStatus.OK));

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
