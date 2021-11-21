package com.davistiba.wedemyserver.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {
    private Integer id;
    private String fullname;
    private String email;
    private Instant datejoined;

    /**
     * hide some email chars with star (*)
     *
     * @return 'masked' string
     */
    public String getEmail() {
        char[] baba = email.toCharArray();
        int limit = email.indexOf('@');
        for (int i = 1; i < limit; i++) {
            baba[i] = '*';
        }
        return new String(baba);
    }
}
