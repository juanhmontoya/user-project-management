package com.demo.userprojectmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.demo.userprojectmanagement.dto.ProjectDTO;
import com.demo.userprojectmanagement.exception.ProjectAssignException;
import com.demo.userprojectmanagement.exception.ProjectNameNotValidException;
import com.demo.userprojectmanagement.exception.ProjectNotFoundException;
import com.demo.userprojectmanagement.exception.ProjectUnassignException;
import com.demo.userprojectmanagement.exception.ProjectsNotLoadedException;
import com.demo.userprojectmanagement.model.Project;
import com.demo.userprojectmanagement.model.User;
import com.demo.userprojectmanagement.repository.ProjectRepository;
import com.demo.userprojectmanagement.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private ProjectRepository projectRepository;

  @InjectMocks private ProjectService service;

  @Test
  public void saveProject_Success() {
    // Given
    String name = "JEP-390";
    String description = "Warnings for Value-Based Classes";
    var project = Project.builder().name(name).description(description).build();

    // When
    when(projectRepository.save(project)).thenReturn(project);

    // Then
    var savedProject = service.create(project);

    assertNotNull(savedProject);
    assertEquals("JEP-390", savedProject.getName());
    assertEquals("Warnings for Value-Based Classes", savedProject.getDescription());
    verify(projectRepository, times(1)).save(project);
  }

  @Test
  public void saveProject_nullName_Failed() {
    // Given
    var project = Project.builder().name(null).build();

    // Then
    assertThrows(ProjectNameNotValidException.class, () -> service.create(project));
    verify(projectRepository, never()).save(project);
  }

  @Test
  public void saveProject_emptyName_Failed() {
    // Given
    var project = Project.builder().name("  ").build();

    // Then
    assertThrows(ProjectNameNotValidException.class, () -> service.create(project));
    verify(projectRepository, never()).save(project);
  }

  @Test
  public void findById_Success() {
    // Given
    long projectId = 1L;
    String name = "JEP-390";
    String description = "Warnings for Value-Based Classes";
    var project = Project.builder().id(projectId).name(name).description(description).build();

    // When
    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

    // Then
    var actualProject = service.findById(projectId);

    assertEquals(project, actualProject);
    verify(projectRepository, times(1)).findById(projectId);
  }

  @Test
  public void findById_projectNotFound_Failed() {
    // Given
    long projectId = 1L;
    when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

    // Then
    assertThrows(ProjectNotFoundException.class, () -> service.findById(projectId));
    verify(projectRepository, times(1)).findById(projectId);
  }

  @Test
  public void testFindByName_Success() {
    // Given
    long projectId = 1L;
    String name = "JEP-390";
    String description = "Warnings for Value-Based Classes";
    var project = Project.builder().id(projectId).name(name).description(description).build();

    // When
    when(projectRepository.findByName("JEP-390")).thenReturn(Optional.of(project));

    // Then
    ProjectDTO result = service.findByName(name);

    assertNotNull(result);
    assertEquals(project.getId(), result.getId());
    assertEquals(project.getName(), result.getName());
    assertEquals(project.getDescription(), result.getDescription());
    verify(projectRepository, times(1)).findByName(name);
  }

  @Test
  public void testFindByName_notFound_Failed() {
    // Given
    String projectName = "not valid projeect";
    when(projectRepository.findByName(projectName)).thenReturn(Optional.empty());

    // Then
    assertThrows(ProjectNotFoundException.class, () -> service.findByName(projectName));
    verify(projectRepository, times(1)).findByName(projectName);
  }

  @Test
  public void testFindAll_Success() {
    // Given
    List<Project> projectList = new ArrayList<>();

    long projectId1 = 1L;
    String name1 = "JEP-390";
    String description1 = "Warnings for Value-Based Classes";
    var project1 = Project.builder().id(projectId1).name(name1).description(description1).build();

    long projectId2 = 2L;
    String name2 = "JEP-181";
    String description2 = "Nest-Based Access Control";
    var project2 = Project.builder().id(projectId2).name(name2).description(description2).build();

    projectList.add(project1);
    projectList.add(project2);
    Pageable pageable = Pageable.ofSize(10);

    // When
    when(projectRepository.findAll(pageable))
        .thenReturn(new PageImpl<>(projectList, pageable, projectList.size()));

    // Then
    Page<ProjectDTO> result = service.findAll(pageable);

    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertEquals(2, result.getContent().size());
    assertEquals(name1, result.getContent().get(0).getName());
    assertEquals(description1, result.getContent().get(0).getDescription());
    assertEquals(name2, result.getContent().get(1).getName());
    assertEquals(description2, result.getContent().get(1).getDescription());
    verify(projectRepository, times(1)).findAll(pageable);
  }

  @Test
  public void testFindAll_projectsNotFound_Failed() {
    // Given
    List<Project> projectList = new ArrayList<>();
    Pageable pageable = Pageable.ofSize(10);
    when(projectRepository.findAll(pageable)).thenReturn(new PageImpl<>(projectList, pageable, 0));

    // Then
    assertThrows(ProjectsNotLoadedException.class, () -> service.findAll(pageable));
    verify(projectRepository, times(1)).findAll(pageable);
  }

  @Test
  public void testUpdate_Success() {
    // Given
    long projectId = 1L;
    String originalName = "JEP-390";
    String originalDescription = "Warnings for Value-Based Classes";
    var originalProject =
        Project.builder().id(projectId).name(originalName).description(originalDescription).build();

    // When
    when(projectRepository.findById(projectId)).thenReturn(Optional.of(originalProject));

    String updatedName = "JEP-3903";
    String updatedDescription = "Warnings for Value-Based Classes and JPA Entities";
    var updatedProject =
        Project.builder().id(projectId).name(updatedName).description(updatedDescription).build();

    when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

    // Then
    var result = service.update(updatedProject, projectId);

    assertNotNull(result);
    assertEquals(projectId, result.getId());
    assertEquals(updatedName, result.getName());
    assertEquals(updatedDescription, result.getDescription());
    verify(projectRepository, times(1)).findById(projectId);
    verify(projectRepository, times(1)).save(originalProject);
  }

  @Test
  public void testUpdate_projectNotFound_Failed() {
    // Given
    long projectId = 1L;

    // When
    when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

    String updatedName = "JEP-121";
    String updatedDescription = "Sealed classes";
    var updatedProject =
        Project.builder().id(projectId).name(updatedName).description(updatedDescription).build();

    // Then
    assertThrows(ProjectNotFoundException.class, () -> service.update(updatedProject, projectId));

    verify(projectRepository, times(1)).findById(projectId);
    verify(projectRepository, never()).save(any());
  }

  @Test
  public void testAssignUsers_Success() {
    // Given
    long projectId = 1L;
    List<Long> userIds = Arrays.asList(1L, 2L);
    var project = Project.builder().id(projectId).build();

    User user1 = new User();
    user1.setId(1L);

    User user2 = new User();
    user2.setId(2L);

    List<User> users = Arrays.asList(user1, user2);

    // When
    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
    when(userRepository.findAllById(userIds)).thenReturn(users);
    when(projectRepository.save(project)).thenReturn(project);

    // Then
    var result = service.assignUsersToProject(projectId, userIds);

    assertNotNull(result);
    assertEquals(users, result.getUsers());
    verify(projectRepository, times(1)).findById(projectId);
    verify(userRepository, times(1)).findAllById(userIds);
    verify(projectRepository, times(1)).save(project);
  }

  @Test
  public void testAssignUsers_projectNotFound_Failed() {
    // Given
    long projectId = 1L;
    List<Long> userIds = Arrays.asList(1L, 2L);

    // When
    when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

    // Then
    assertThrows(
        ProjectNotFoundException.class, () -> service.assignUsersToProject(projectId, userIds));

    verify(projectRepository, times(1)).findById(projectId);
    verifyNoMoreInteractions(userRepository);
    verifyNoMoreInteractions(projectRepository);
  }

  @Test
  public void testAssignUsers_usersNotFound_Failed() {
    // Given
    long projectId = 1L;
    List<Long> userIds = Arrays.asList(1L, 2L);
    var project = Project.builder().id(projectId).build();

    User user = new User();
    user.setId(1L);
    List<User> users = List.of(user);

    // When
    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
    when(userRepository.findAllById(userIds)).thenReturn(users);

    // Then
    assertThrows(
        ProjectAssignException.class, () -> service.assignUsersToProject(projectId, userIds));

    verify(projectRepository, times(1)).findById(projectId);
    verify(userRepository, times(1)).findAllById(userIds);
    verifyNoMoreInteractions(projectRepository);
  }

  @Test
  public void testUnassignUsers_Success() {
    // Given
    long projectId = 1L;
    List<Long> userIds = List.of(1L);
    var project = Project.builder().id(projectId).build();

    User projectUser1 = new User();
    projectUser1.setId(1L);

    User projectUser2 = new User();
    projectUser2.setId(2L);

    User existingUser1 = new User();
    existingUser1.setId(1L);

    List<User> projectUsers = new ArrayList<>();
    projectUsers.add(projectUser1);
    projectUsers.add(projectUser2);

    project.setUsers(projectUsers);
    List<User> existingUsers = List.of(existingUser1);

    // When
    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
    when(userRepository.findAllById(userIds)).thenReturn(existingUsers);
    when(projectRepository.save(project)).thenReturn(project);

    // Then
    var result = service.unassignUsersFromProject(projectId, userIds);

    assertNotNull(result);
    assertEquals(project, result);
    assertEquals(result.getUsers().size(), 1);
    verify(projectRepository, times(1)).findById(projectId);
    verify(userRepository, times(1)).findAllById(userIds);
    verify(projectRepository, times(1)).save(project);
  }

  @Test
  public void testUnassignUsers_noProjectFound_Failed() {
    // Given
    long id = 1L;
    List<Long> userIds = Arrays.asList(1L, 2L);

    // When
    when(projectRepository.findById(id)).thenReturn(Optional.empty());

    // Then
    assertThrows(
        ProjectNotFoundException.class, () -> service.unassignUsersFromProject(id, userIds));
    verify(projectRepository, times(1)).findById(id);
    verifyNoMoreInteractions(projectRepository);
  }

  @Test
  public void testUnassignUsers_noUserFound_Failed() {
    // Given
    long id = 1L;
    List<Long> userIds = Arrays.asList(1L, 2L);
    User user = new User();
    user.setId(id);
    List<User> userList = new ArrayList<>();
    userList.add(user);

    var existingProject = new Project();
    existingProject.setId(id);
    existingProject.setUsers(userList);

    // When
    when(projectRepository.findById(id)).thenReturn(Optional.of(existingProject));

    // Then
    assertThrows(
        ProjectUnassignException.class, () -> service.unassignUsersFromProject(id, userIds));
    verify(projectRepository, times(1)).findById(id);
    verifyNoMoreInteractions(projectRepository);
  }

  @Test
  public void deleteProject_Success() {
    // Given
    long projectId = 1L;
    var project = Project.builder().id(projectId).build();
    var user1 = new User();
    var user2 = new User();

    List<User> userList = new ArrayList<>();
    userList.add(user1);
    userList.add(user2);
    project.setUsers(userList);

    Set<Project> projects = new HashSet<>();
    projects.add(project);
    user1.setProjects(projects);
    user2.setProjects(projects);

    // When
    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

    // Then
    service.delete(projectId);

    verify(projectRepository, times(1)).findById(projectId);
    verify(projectRepository, times(1)).deleteById(projectId);
  }

  @Test
  public void deleteProject_noProjectFound_Failed() {
    // Given
    long id = 1L;

    // When
    when(projectRepository.findById(id)).thenReturn(Optional.empty());

    // Then
    assertThrows(ProjectNotFoundException.class, () -> service.delete(id));
    verify(projectRepository, times(1)).findById(id);
    verifyNoMoreInteractions(projectRepository);
  }
}
