package com.demo.userprojectmanagement.service;

import com.demo.userprojectmanagement.dto.ProjectDTO;
import com.demo.userprojectmanagement.dto.UserDTO;
import com.demo.userprojectmanagement.exception.UserEmailExistsException;
import com.demo.userprojectmanagement.exception.UserEmailNotFoundException;
import com.demo.userprojectmanagement.exception.UserEmailNotValidException;
import com.demo.userprojectmanagement.exception.UserFieldNotValidException;
import com.demo.userprojectmanagement.exception.UserIdNotFoundException;
import com.demo.userprojectmanagement.exception.UserNameNotFoundException;
import com.demo.userprojectmanagement.exception.UsersNotCreatedException;
import com.demo.userprojectmanagement.model.Project;
import com.demo.userprojectmanagement.model.User;
import com.demo.userprojectmanagement.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private static final String EMAIL_REGEX = "^[\\w.-]+@[a-zA-Z_-]+?(?:\\.[a-zA-Z]{2,6})+$";
  private final UserRepository repository;

  @Autowired
  public UserService(UserRepository userRepository) {
    repository = userRepository;
  }

  /**
   * Finds user according to a given name.
   *
   * @param name string
   * @return user entity
   */
  public User findByName(String name) {
    return repository.findUserByName(name).orElseThrow(() -> new UserNameNotFoundException(name));
  }

  /**
   * Finds user according to a given id.
   *
   * @param id of user
   * @return user DTO entity with the assigned projects if any
   */
  public UserDTO findById(long id) {
    var user = repository.findById(id).orElseThrow(() -> new UserIdNotFoundException(id));
    Set<ProjectDTO> projectsDTO;

    if (user.getProjects() != null && user.getProjects().size() > 0) {
      projectsDTO =
          user.getProjects().stream()
              .map(p -> new ProjectDTO(p.getId(), p.getName(), p.getDescription()))
              .collect(Collectors.toSet());
    } else {
      projectsDTO = new HashSet<>();
    }

    return new UserDTO(user.getId(), user.getName(), user.getEmail(), projectsDTO);
  }

  /**
   * Finds user according to a given email.
   *
   * @param email string
   * @return user entity
   */
  public User findByEmail(String email) {
    if (!isValidEmail(email)) throw new UserEmailNotValidException();

    return repository
        .findUserByEmail(email)
        .orElseThrow(() -> new UserEmailNotFoundException(email));
  }

  /**
   * Gets all the users in the database
   *
   * @param pageable indicates the pagination settings. If not provided default page size is 10 and
   *     current-page is 0.
   * @return a page with all the users in the records according to the pagination configurations
   */
  public Page<User> findAll(Pageable pageable) {
    var users = repository.findAll(pageable);

    if (!users.isEmpty()) {
      return users;
    } else {
      throw new UsersNotCreatedException();
    }
  }

  /**
   * Creates and persists new user entity in the users table.
   *
   * @param user with no id
   * @return the created user
   */
  public User create(User user) {
    if (!isValidEmail(user.getEmail())) throw new UserEmailNotValidException();

    if (userExists(user.getEmail())) throw new UserEmailExistsException();

    if (user.getName() == null || user.getName().isBlank())
      throw new UserFieldNotValidException("name");

    return repository.save(user);
  }

  /**
   * Modifies an existing user with new data.
   *
   * @param user entity
   * @return the existing user with the updated fields
   */
  public User update(User user) {
    var userOptional = repository.findById(user.getId());

    if (userOptional.isEmpty()) throw new UserIdNotFoundException(user.getId());
    if (user.getEmail() == null || !isValidEmail(user.getEmail()))
      throw new UserEmailNotValidException();
    if (user.getName() == null || user.getName().isBlank())
      throw new UserFieldNotValidException("name");

    var updatedUser = userOptional.get();
    updatedUser.setEmail(user.getEmail());
    updatedUser.setName(user.getName());
    updatedUser.setProjects(user.getProjects());

    return repository.save(updatedUser);
  }

  /**
   * Deletes an existing user from the records.
   *
   * @param id of user
   */
  @Transactional
  public void delete(long id) {
    var userOptional = repository.findById(id);

    if (userOptional.isEmpty()) throw new UserIdNotFoundException(id);

    var user = userOptional.get();

    if (user.getProjects().size() > 0) {
      for (Project p : user.getProjects()) {
        p.getUsers().remove(user);
      }
    }

    user.getProjects().clear();

    repository.deleteById(id);
  }

  /**
   * Check if the given email is properly formatted.
   *
   * @param email string
   * @return true if the email format is correct
   */
  private boolean isValidEmail(String email) {
    return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
  }

  /**
   * Will check if the given email is already existent in records.
   *
   * @param email string
   * @return true if the given email exists in the database
   */
  private boolean userExists(String email) {
    Optional<User> u = repository.findUserByEmail(email);
    return u.isPresent();
  }
}
