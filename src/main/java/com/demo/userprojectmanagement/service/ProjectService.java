package com.demo.userprojectmanagement.service;

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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
  }

  /**
   * Find project according to a given id.
   *
   * @param id of the project
   * @return project entity
   */
  public Project findById(Long id) {
    return projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
  }

  /**
   * Find project according to a name.
   *
   * @param name of the project
   * @return project entity
   */
  public ProjectDTO findByName(String name) {
    Project project = projectRepository.findByName(name).orElseThrow(ProjectNotFoundException::new);

    return new ProjectDTO(project.getId(), project.getName(), project.getDescription());
  }

  /**
   * Retrieves all the registered projects paged accordingly.
   *
   * @param pageable indicates the pagination settings. If not provided default page size is 10 and
   *     current-page is 0.
   * @return a page with all the projects in the database according to the pagination params
   *     provided
   */
  public Page<ProjectDTO> findAll(Pageable pageable) {
    var projects = projectRepository.findAll(pageable);

    if (projects.isEmpty()) throw new ProjectsNotLoadedException();

    var projectDTOS =
        projects.stream()
            .map(p -> new ProjectDTO(p.getId(), p.getName(), p.getDescription()))
            .collect(Collectors.toList());

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), projectDTOS.size());

    return new PageImpl<>(projectDTOS.subList(start, end), pageable, projectDTOS.size());
  }

  /**
   * Creates and persists a new project entity in the database.
   *
   * @param project entity
   * @return the new created project
   */
  public Project create(Project project) {
    if (project.getName() == null || project.getName().isBlank())
      throw new ProjectNameNotValidException();

    return projectRepository.save(project);
  }

  /**
   * Updates a given project with new data.
   *
   * @param project entity
   * @return the project with updated fields.
   */
  public Project update(Project project, long id) {
    var projectOptional = projectRepository.findById(id);

    if (projectOptional.isEmpty()) throw new ProjectNotFoundException();

    var updatedProject = projectOptional.get();

    if (project.getName() != null) updatedProject.setName(project.getName());

    if (project.getDescription() != null) updatedProject.setDescription(project.getDescription());

    return projectRepository.save(updatedProject);
  }

  /**
   * Assigns userId/s to a given project.
   *
   * @param id project id
   * @param userIds list of Ids to be assigned to the project
   * @return project with assigned users
   */
  public Project assignUsersToProject(long id, List<Long> userIds) {
    var projectOptional = projectRepository.findById(id);

    if (projectOptional.isEmpty()) throw new ProjectNotFoundException();

    var users = userRepository.findAllById(userIds);

    if (userIds.size() != users.size())
      throw new ProjectAssignException(buildNotValidUsersString(users, userIds));

    var project = projectOptional.get();

    project.setUsers(users);

    return projectRepository.save(project);
  }

  /**
   * Unassigns given users from a project based on provided user id/s.
   *
   * @param id of the project
   * @param userIds collection that will be unassigned from a project
   * @return project with the updated user collection
   */
  public Project unassignUsersFromProject(long id, List<Long> userIds) {
    var projectOptional = projectRepository.findById(id);
    var users = userRepository.findAllById(userIds);

    if (projectOptional.isEmpty()) throw new ProjectNotFoundException();

    var project = projectOptional.get();
    var currentUsers = project.getUsers();
    var currentUsersIds = currentUsers.stream().map(User::getId).collect(Collectors.toSet());

    if (!currentUsersIds.containsAll(userIds))
      throw new ProjectUnassignException(buildNotValidUsersString(currentUsers, userIds));

    if (currentUsers.size() > 0) {
      currentUsers.removeAll(users);
    }

    project.setUsers(currentUsers);

    return projectRepository.save(project);
  }

  /**
   * Deletes a project from the database.
   *
   * @param id of the project
   */
  @Transactional
  public void delete(long id) {
    var projectOptional = projectRepository.findById(id);

    if (projectOptional.isEmpty()) throw new ProjectNotFoundException();

    var project = projectOptional.get();

    project.getUsers().clear();

    projectRepository.deleteById(id);
  }

  /**
   * Builds a string with the id/s that are not existing in the database or assigned to a given
   * project.
   *
   * @param users list
   * @param userIds that represents the id/s to compare
   * @return sequence of id/s in string format
   */
  private String buildNotValidUsersString(List<User> users, List<Long> userIds) {
    StringBuilder notAssignedIds = new StringBuilder();

    List<Long> actual = users.stream().map(User::getId).collect(Collectors.toList());

    List<Long> difference =
        userIds.stream().filter(e -> !actual.contains(e)).collect(Collectors.toList());

    for (Long notFoundId : difference) {
      notAssignedIds.append(notFoundId.toString());
      notAssignedIds.append(",");
    }
    notAssignedIds.deleteCharAt(notAssignedIds.length() - 1);

    return notAssignedIds.toString();
  }
}
