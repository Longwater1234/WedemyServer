package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String fullname;
    private String email;
    private Instant createdAt;


    public UserDTO(Integer id, String fullname, String email, Instant createdAt) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.createdAt = createdAt;
    }
}
