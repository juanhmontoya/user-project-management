package com.demo.userprojectmanagement.dto;

import java.util.Set;
import lombok.Data;

@Data
public class UserDTO {
  private long id;
  private String name;
  private String email;
  private Set<ProjectDTO> projects;

  public UserDTO(long id, String name, String email, Set<ProjectDTO> projects) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.projects = projects;
  }
}
