package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This conveniently returns login status and userInfo summary (from session cache). Queried very frequently
 *
 * @since 2023-08-13
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginStatus {
    private boolean loggedIn = false;
    private UserDTO userInfo;


    public LoginStatus(boolean loggedIn, UserDTO userInfo) {
        this.loggedIn = loggedIn;
        this.userInfo = userInfo;
    }
}
