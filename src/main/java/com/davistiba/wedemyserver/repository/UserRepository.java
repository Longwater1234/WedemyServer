package com.davistiba.wedemyserver.repository;

import java.util.List;

import com.davistiba.wedemyserver.models.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
  List<User> findByFullname(String firstName);

}
