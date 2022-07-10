package com.davistiba.wedemyserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String fullname;
    private String email;
    private Instant createdAt;

    public UserDTO() {
    }
}
