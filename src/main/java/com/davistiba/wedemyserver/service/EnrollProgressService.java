package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.WatchStatus;
import com.davistiba.wedemyserver.models.EnrollProgress;
import com.davistiba.wedemyserver.models.Enrollment;
import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.repository.EnrollProgressRepository;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

@Service
public class EnrollProgressService {

    @Autowired
    private EnrollProgressRepository progressRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public static final Logger logger = LoggerFactory.getLogger(EnrollProgressService.class);

    /**
     * Update tables, then return next lesson
     *
     * @param status     watchStatus
     * @param enrollment current
     * @return next Lesson
     */
    @Transactional
    public Lesson updateAndGetNextLesson(@NotNull WatchStatus status, Enrollment enrollment) {
        UUID lessonId = UUID.fromString(status.getCurrentLessonId());
        Lesson currentLesson = lessonRepository.findLessonById(lessonId).orElseThrow();
        Optional<EnrollProgress> enrollProgress = progressRepository.findByEnrollIdAndLessonId(status.getEnrollId(), lessonId);

        if (enrollProgress.isEmpty()) {
            //INSERT into progress table
            EnrollProgress progress = progressRepository.save(new EnrollProgress(enrollment, currentLesson));

            //calculate percent progress
            long currentPosition = currentLesson.getPosition();
            long totalLessons = lessonRepository.countByCourseId(status.getCourseId());
            double valDouble = (double) currentPosition / (double) totalLessons * 100.00;

            //update `Enrollments` table
            logger.info("percent {}", valDouble);
            BigDecimal progressPercent = BigDecimal.valueOf(valDouble).setScale(2, RoundingMode.FLOOR);
            enrollment.setCurrentLessonId(lessonId);
            enrollment.setProgress(progressPercent);
            enrollmentRepository.save(enrollment);

            //get next lesson
            Integer courseId = progress.getLesson().getCourse().getId();
            Integer nextPosition = progress.getLesson().getPosition() + 1;
            return lessonRepository.findByCourseIdAndPosition(courseId, nextPosition).orElseThrow();
        }

        //OTHERWISE, simply RETURN NEXT.
        Integer courseId = status.getCourseId();
        Integer nextPosition = currentLesson.getPosition() + 1;
        return lessonRepository.findByCourseIdAndPosition(courseId, nextPosition).orElseThrow();

    }

}
