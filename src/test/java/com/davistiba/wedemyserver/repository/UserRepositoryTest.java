package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.AuthProvider;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.models.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void contextLoads() {
    }

    static ArrayList<User> getUserList() {
        ArrayList<User> userList = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setFullname("hello_" + ThreadLocalRandom.current().nextInt(999));
            user.setEmail("hello@pp" + ThreadLocalRandom.current().nextInt(99889) + "yahoo.com");
            user.setConfirmPass("WHATEVER!"); //<-- anything, but not NULL
            user.setEnabled(true);
            user.setAuthProvider(AuthProvider.GOOGLE);
            user.setUserRole(UserRole.ROLE_STUDENT);
            userList.add(user);
        }
        return userList;
    }

    @Test
    @Transactional
    public void batchInsertTest_JDBC() {
        ArrayList<User> userList = getUserList();

        log.info("starting");
        jdbcTemplate.batchUpdate("INSERT INTO users (fullname, email, enabled, auth_provider, user_role, created_at) " +
                "VALUES (?, ? , ? , ?, ?, ?)", userList, 100, (ps, argument) -> {
            int col = 1;
            ps.setString(col++, argument.getFullname());
            ps.setString(col++, argument.getEmail());
            ps.setBoolean(col++, argument.getEnabled());
            ps.setString(col++, argument.getAuthProvider().name());
            ps.setString(col++, argument.getUserRole());
            ps.setDate(col++, new java.sql.Date(Instant.now().toEpochMilli()));
        });

        log.info("ending");
    }

    @Test
    @Transactional
    // SLOW 6x than previous JDBC method
    public void batchInsertTest_JPA() {
        ArrayList<User> userList = getUserList();
        log.info("========== starting saveAll JPA =============");
        userRepository.saveAll(userList);
        log.info("ENDING");
    }
}