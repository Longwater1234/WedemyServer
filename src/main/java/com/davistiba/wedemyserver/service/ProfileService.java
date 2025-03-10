package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.StudentSummary;
import com.davistiba.wedemyserver.models.SummaryTitle;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    /**
     * Custom service to return a Student's summary
     */
    public List<StudentSummary> getUserSummaryList(@NotNull User user) {
        ArrayList<StudentSummary> summaryList = new ArrayList<>(3);
        long owned = enrollmentRepository.countEnrollmentByUser(user);
        StudentSummary s1 = new StudentSummary(SummaryTitle.OWNING, owned, "courses");
        summaryList.add(s1);

        long completed = enrollmentRepository.countEnrollmentByUserAndIsCompleted(user, true);
        StudentSummary s2 = new StudentSummary(SummaryTitle.COMPLETED, completed, "courses");
        summaryList.add(s2);

        Duration duration = Duration.between(Instant.now(), user.getCreatedAt()).abs();
        StudentSummary s3 = this.getMembershipTime(duration);
        summaryList.add(s3);
        return summaryList;
    }

    private StudentSummary getMembershipTime(final Duration duration) {
        final long numberDays = duration.toDays();

        long result = 0;
        String units;
        if (numberDays == 0) units = "today";
        else if (numberDays > 0 && numberDays <= 30) {
            result = numberDays;
            units = "days ago";
        } else if (numberDays > 30 && numberDays <= 365) {
            result = Math.floorDiv(duration.toDays(), 30);
            units = "month(s) ago";
        } else {
            result = Math.floorDiv(duration.toDays(), 365);
            units = "year(s) ago";
        }

        return new StudentSummary(SummaryTitle.JOINED, result, units);
    }
}
