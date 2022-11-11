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
    public Lesson updateAndGetNextLesson(@NotNull WatchStatus status, Enrollment enrollment) {
        UUID lessonId = UUID.fromString(status.getCurrentLessonId());
        Lesson currentLesson = lessonRepository.findLessonById(lessonId).orElseThrow();
        Optional<EnrollProgress> enrollProgress = progressRepository.findByEnrollIdAndLessonId(status.getEnrollId(), lessonId);

        if (enrollProgress.isEmpty()) {
            //MEANING: User hasn't ALREADY watched this
            EnrollProgress progress = progressRepository.save(new EnrollProgress(enrollment, currentLesson));

            //FIXME calculate percent progress
            long currentPosition = currentLesson.getPosition();
            long totalLessons = lessonRepository.countByCourseId(status.getCourseId());
            double progDouble = (double) currentPosition / (double) totalLessons * 100.00;
            boolean isCompleted = (progDouble / 100.00) == 1;

            //update `Enrollments` table
            BigDecimal progressPercent = BigDecimal.valueOf(progDouble).setScale(2, RoundingMode.FLOOR);
            enrollment.setProgress(progressPercent);
            enrollment.setIsCompleted(isCompleted);
            if (!isCompleted) {
                enrollment.setNextPosition(currentLesson.getPosition() + 1);
            }
            enrollmentRepository.save(enrollment);

            //get next lesson
            int courseId = status.getCourseId();
            int nextPosition = progress.getLesson().getPosition() + 1;
            return lessonRepository.findByCourseIdAndPosition(courseId, nextPosition).orElse(null);
        }

        //OTHERWISE, simply RETURN NEXT.
        int courseId = status.getCourseId();
        int nextPosition = currentLesson.getPosition() + 1;
        return lessonRepository.findByCourseIdAndPosition(courseId, nextPosition).orElse(null);

    }


    /**
     * Get the next lesson for this Enrollment
     *
     * @param enrollment current
     * @return next lessonId
     */
    public Lesson getNextLesson(@NotNull Enrollment enrollment) {
        Integer nextPosition = enrollment.getNextPosition();
        return lessonRepository.findByCourseIdAndPosition(enrollment.getCourse().getId(), nextPosition).orElseThrow();
    }

}
