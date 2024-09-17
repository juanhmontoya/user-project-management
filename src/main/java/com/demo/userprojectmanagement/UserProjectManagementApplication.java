package com.demo.userprojectmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.demo.userprojectmanagement.repository")
@ComponentScan(basePackages = "com.demo.userprojectmanagement")
@EntityScan(basePackages = "com.demo.userprojectmanagement.model")
public class UserProjectManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserProjectManagementApplication.class, args);
  }
}
