package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.UserSummary;
import com.davistiba.wedemyserver.models.SummaryTitle;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    /**
     * Custom service to return Student summary
     *
     * @param user User
     * @return list
     */
    public List<UserSummary> getUserSummaryList(@NotNull User user) {
        List<UserSummary> summaryList = new ArrayList<>();
        long owned = enrollmentRepository.countEnrollmentByUser(user);
        UserSummary s1 = new UserSummary(SummaryTitle.OWNING, owned, "courses");
        summaryList.add(s1);

        long completed = enrollmentRepository.countEnrollmentByUserAndIsCompleted(user, true);
        UserSummary s2 = new UserSummary(SummaryTitle.COMPLETED, completed, "courses");
        summaryList.add(s2);

        Duration duration = Duration.between(Instant.now(), user.getCreatedAt()).abs();
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

        UserSummary s3 = new UserSummary(SummaryTitle.JOINED, result, units);
        summaryList.add(s3);

        return summaryList;
    }
}
