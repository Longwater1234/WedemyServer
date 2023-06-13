package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.WatchStatus;
import com.davistiba.wedemyserver.models.EnrollProgress;
import com.davistiba.wedemyserver.models.Enrollment;
import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.repository.EnrollProgressRepository;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
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


    /**
     * Update tables, then return next lesson
     *
     * @param status     watchStatus
     * @param enrollment current
     * @return next Lesson
     */
    @Transactional
    public Optional<Lesson> updateAndGetNextLesson(@NotNull WatchStatus status, Enrollment enrollment) {
        UUID lessonId = UUID.fromString(status.getCurrentLessonId());
        Lesson currentLesson = lessonRepository.findById(lessonId).orElseThrow();
        Optional<EnrollProgress> enrollProgress = progressRepository.findByEnrollIdAndLessonId(status.getEnrollId(), lessonId);

        if (enrollProgress.isEmpty()) {
            //means User has NOT ALREADY watched this
            progressRepository.save(new EnrollProgress(enrollment, currentLesson));

            long numWatched = progressRepository.countByEnrollmentId(enrollment.getId());
            long totalLessons = lessonRepository.countByCourseId(status.getCourseId());
            double progDouble = (double) numWatched / (double) totalLessons * 100.00;
            boolean isCompleted = (progDouble / 100.00) == 1;

            //update `Enrollments` table
            BigDecimal progressPercent = BigDecimal.valueOf(progDouble).setScale(2, RoundingMode.FLOOR);
            enrollment.setProgress(progressPercent);
            enrollment.setIsCompleted(isCompleted);
            if (!isCompleted) {
                enrollment.setNextPosition(currentLesson.getPosition() + 1);
            } else {
                enrollment.setNextPosition(1); //reset to 1
            }
            enrollmentRepository.save(enrollment);
            return this.getNextLesson(enrollment);
        }

        //OTHERWISE, simply RETURN NEXT.
        return this.getNextLesson(enrollment);

    }


    /**
     * Get the next lesson for this Enrollment
     *
     * @param enrollment current
     * @return next lessonId
     */
    public Optional<Lesson> getNextLesson(@NotNull Enrollment enrollment) {
        Integer nextPosition = enrollment.getNextPosition();
        Integer courseId = enrollment.getCourse().getId();
        Optional<Lesson> next = lessonRepository.getFirstNotWatchedByCourseId(enrollment.getId(), courseId);
        return next.or(() -> lessonRepository.findByCourseIdAndPosition(courseId, nextPosition));
    }

}
