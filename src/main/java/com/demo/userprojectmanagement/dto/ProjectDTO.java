package com.demo.userprojectmanagement.dto;

import lombok.Data;

@Data
public class ProjectDTO {
  private long id;
  private String name;
  private String description;

  public ProjectDTO(long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
