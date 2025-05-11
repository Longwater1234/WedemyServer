package com.davistiba.wedemyserver.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void existsByUserIdAndCourseId_Test() {
        Assertions.assertTrue(enrollmentRepository.existsByUserIdAndCourseId(8, 10018));
    }
}