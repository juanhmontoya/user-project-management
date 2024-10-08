package com.demo.userprojectmanagement.repository;

import com.demo.userprojectmanagement.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Page<User> findAll(Pageable pageable);

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByName(String name);
}
