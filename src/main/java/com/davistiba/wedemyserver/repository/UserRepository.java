package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("SELECT new com.davistiba.wedemyserver.dto.UserDTO(id, fullname, email, datejoined) FROM User WHERE id = ?1")
    Optional<UserDTO> findUserDTObyId(Integer id);


}
