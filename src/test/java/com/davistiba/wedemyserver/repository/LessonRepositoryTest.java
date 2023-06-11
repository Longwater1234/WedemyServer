package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Lesson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    void getFirstNotWatchedByCourseId_Test() {
        Optional<Lesson> optionalLesson = lessonRepository.getFirstNotWatchedByCourseId(2L, 10013);
        Assertions.assertTrue(optionalLesson.isPresent());
    }
}