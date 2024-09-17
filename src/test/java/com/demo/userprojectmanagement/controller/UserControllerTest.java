package com.demo.userprojectmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.demo.userprojectmanagement.dto.UserDTO;
import com.demo.userprojectmanagement.model.User;
import com.demo.userprojectmanagement.service.UserService;
import com.demo.userprojectmanagement.util.Util;
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
public class UserControllerTest {
  @Mock private UserService userService;

  @InjectMocks private UserController userController;

  @Test
  public void testFindAll_Success() {
    // Given
    Pageable pageable = Pageable.ofSize(10).withPage(0);
    User user1 = new User();
    user1.setId(1L);
    user1.setName("Robert");

    User user2 = new User();
    user2.setId(2L);
    user2.setName("Jack");
    Page<User> users = new PageImpl<>(List.of(user1, user2));

    // When
    when(userService.findAll(pageable)).thenReturn(users);

    // Then
    ResponseEntity<?> response = userController.findAll(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(Util.convertToResponse(users, "users"), response.getBody());
    verify(userService, times(1)).findAll(pageable);
  }

  @Test
  public void testFindByNameAndEmail_argumentName_Success() {
    // Given
    String name = "Brian";
    User user = new User();
    user.setName(name);

    // When
    when(userService.findByName(name)).thenReturn(user);

    // Then
    ResponseEntity<?> response = userController.findByNameAndEmail(name, null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(user, response.getBody());
    verify(userService, times(1)).findByName(name);
  }

  @Test
  public void testFindByNameAndEmail_argumentEmail_Success() {
    // Given
    String name = "Brian";
    String email = "bgoetz@example.com";
    User user = new User();
    user.setName(name);
    user.setEmail(email);

    // When
    when(userService.findByEmail(email)).thenReturn(user);

    // Then
    ResponseEntity<?> response = userController.findByNameAndEmail(null, email);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(user, response.getBody());
    verify(userService, times(1)).findByEmail(email);
  }

  @Test
  public void testFindById_Success() {
    // Given
    long id = 1L;
    UserDTO user = new UserDTO(id, "Robert", "rob@gmail.com", null);
    user.setId(id);

    // When
    when(userService.findById(id)).thenReturn(user);

    // Then
    ResponseEntity<?> responseEntity = userController.findById(id);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(user, responseEntity.getBody());
    verify(userService, times(1)).findById(id);
  }

  @Test
  public void testCreate_Success() {
    // Given
    User user = new User();
    User createdUser = new User();
    createdUser.setId(1L);

    // When
    when(userService.create(user)).thenReturn(createdUser);

    // Then
    ResponseEntity<?> response = userController.create(user);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(createdUser, response.getBody());
    verify(userService, times(1)).create(user);
  }

  @Test
  public void testUpdate_Success() {
    // Given
    User user = new User();
    user.setId(1L);
    user.setName("Brian");
    user.setEmail("bgoetz@sun.com");

    User updatedUser = new User();
    updatedUser.setId(user.getId());
    updatedUser.setName(user.getName());
    updatedUser.setEmail(user.getEmail());

    // When
    when(userService.update(user)).thenReturn(updatedUser);

    // Then
    ResponseEntity<?> response = userController.update(user);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedUser, response.getBody());
    verify(userService, times(1)).update(user);
  }

  @Test
  public void testDelete_Success() {
    // Given
    long id = 1L;
    doNothing().when(userService).delete(id);

    // Then
    ResponseEntity<?> response = userController.delete(id);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(userService, times(1)).delete(id);
  }
}
