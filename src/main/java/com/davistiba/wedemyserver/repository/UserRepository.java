package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
