package com.davistiba.wedemyserver.repository;

import java.util.List;
import java.util.Optional;

import com.davistiba.wedemyserver.models.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
  @Query(value = "SELECT * FROM USERS WHERE fullname = ?1", nativeQuery = true)
  List<User> findByFullname(String firstName);

  Optional<User> findByEmail(String email);

}
