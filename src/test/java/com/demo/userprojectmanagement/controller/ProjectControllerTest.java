package com.demo.userprojectmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.demo.userprojectmanagement.dto.ProjectDTO;
import com.demo.userprojectmanagement.model.Project;
import com.demo.userprojectmanagement.service.ProjectService;
import com.demo.userprojectmanagement.util.Util;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {
  @Mock private ProjectService projectService;

  @InjectMocks private ProjectController projectController;

  @Test
  void testFindAll_Success() {
    // Given
    String name1 = "JEP-121";
    String name2 = "JEP-390";
    String description1 = "Sealed classes";
    String description2 = "Warnings for Value-Based Classes";
    Pageable pageable = Pageable.unpaged();
    List<ProjectDTO> projectDTOList =
        List.of(new ProjectDTO(1L, name1, description1), new ProjectDTO(2L, name2, description2));
    Page<ProjectDTO> projects = new PageImpl<>(projectDTOList, pageable, projectDTOList.size());
    when(projectService.findAll(pageable)).thenReturn(projects);

    // Then
    ResponseEntity<?> response = projectController.findAll(pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody() != null);
    Assertions.assertEquals(Util.convertToResponse(projects, "projects"), response.getBody());
    verify(projectService, times(1)).findAll(pageable);
  }

  @Test
  public void testFindById_Success() {
    // Given
    long id = 1L;
    Project project = new Project();
    project.setId(id);

    // When
    when(projectService.findById(id)).thenReturn(project);

    // Then
    ResponseEntity<?> response = projectController.findById(id);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(project, response.getBody());
    verify(projectService, times(1)).findById(id);
  }

  @Test
  public void testFindByName_Success() {
    // Given
    String name = "JEP-121";
    String description = "Sealed classes";
    ProjectDTO projectDTO = new ProjectDTO(1L, name, description);

    // When
    when(projectService.findByName(name)).thenReturn(projectDTO);

    // Then
    ResponseEntity<?> responseEntity = projectController.findByName(name);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(projectDTO, responseEntity.getBody());
    verify(projectService, times(1)).findByName(name);
  }

  @Test
  public void testCreate_Success() {
    // Given
    Project project = new Project();
    Project createdProject = new Project();

    // When
    when(projectService.create(project)).thenReturn(createdProject);

    // Then
    ResponseEntity<?> response = projectController.create(project);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(createdProject, response.getBody());
    verify(projectService, times(1)).create(project);
  }

  @Test
  public void testUpdate_Success() {
    // Given
    long id = 1L;
    String name = "JEP-121";
    String description = "Sealed classes";
    Project updatedProject = new Project(id, name, description);

    // When
    when(projectService.update(any(Project.class), eq(id))).thenReturn(updatedProject);

    // Then
    ResponseEntity<?> response = projectController.update(updatedProject, id);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedProject, response.getBody());
    verify(projectService, times(1)).update(updatedProject, id);
  }

  @Test
  public void testAssignUsers_Success() {
    // Given
    long id = 1L;
    List<Long> userIds = List.of(1L, 2L);
    Project project = new Project();
    project.setId(id);

    // When
    when(projectService.assignUsersToProject(id, userIds)).thenReturn(project);

    // Then
    ResponseEntity<?> response = projectController.assignUsersToProject(id, userIds);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(project, response.getBody());
    verify(projectService, times(1)).assignUsersToProject(id, userIds);
  }

  @Test
  public void testUnassignUsers_Success() {
    // Given
    long id = 1L;
    List<Long> userIds = Arrays.asList(1L, 2L);
    Project project = new Project();
    project.setId(id);

    // When
    when(projectService.unassignUsersFromProject(id, userIds)).thenReturn(project);

    // Then
    ResponseEntity<?> response = projectController.unassignUsers(id, userIds);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(project, response.getBody());
    verify(projectService, times(1)).unassignUsersFromProject(id, userIds);
  }

  @Test
  public void testDelete_Success() {
    // Given
    long id = 1L;

    // Then
    ResponseEntity<?> response = projectController.delete(id);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(projectService, times(1)).delete(id);
  }
}
