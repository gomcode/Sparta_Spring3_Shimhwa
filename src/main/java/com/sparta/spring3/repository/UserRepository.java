package com.sparta.spring3.repository;

import java.util.Optional;

import com.sparta.spring3.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserId(Long id);
  Optional<User> findByUserName(String name);
}
