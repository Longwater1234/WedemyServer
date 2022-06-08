package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.WatchStatus;
import com.davistiba.wedemyserver.models.EnrollProgress;
import com.davistiba.wedemyserver.models.Enrollment;
import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.repository.EnrollProgressRepository;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class EnrollProgressService {

    @Autowired
    private EnrollProgressRepository progressRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;


    @Transactional
    public Lesson updateAndGetNextLesson(@NotNull WatchStatus status) {
        EnrollProgress progress = progressRepository.saveCustom(status.getEnrollId(), UUID.fromString(status.getCurrentLessonId()));
        //calculate percent progress
        long currentPosition = progress.getLesson().getPosition();
        long totalLessons = lessonRepository.countByCourseId(status.getCourseId());

        //update dB
        BigDecimal progressPercent = BigDecimal.valueOf(currentPosition / totalLessons).multiply(BigDecimal.valueOf(100));
        Enrollment e = enrollmentRepository.findById(status.getEnrollId()).orElseThrow();
        e.setProgress(progressPercent);
        e.setCurrentLessonId(UUID.fromString(status.getCurrentLessonId()));
        enrollmentRepository.save(e);

        //get next lesson
        Integer courseId = progress.getLesson().getCourse().getId();
        Integer nextPosition = progress.getLesson().getPosition() + 1;
        return lessonRepository.findByCourseIdAndPosition(courseId, nextPosition).orElseThrow();
    }
}
