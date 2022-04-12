package com.davistiba.wedemyserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String fullname;
    private String email;
    private Instant createdAt;


}
