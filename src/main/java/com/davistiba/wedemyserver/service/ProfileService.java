package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.UserSummary;
import com.davistiba.wedemyserver.models.SummaryTitle;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private UserRepository userRepository;

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

        Instant dateCreated = userRepository.findById(user.getId()).get().getCreatedAt();
        Period period = Period.between(LocalDate.ofInstant(dateCreated, ZoneId.of("UTC")), LocalDate.now());
        final int numberDays = Math.abs(period.getDays());
        long result = 0;
        String units;
        if (numberDays == 0) units = "today";
        else if (numberDays > 0 && numberDays <= 30) {
            result = numberDays;
            units = "days ago";
        } else if (numberDays > 30 && numberDays <= 365) {
            result = period.getMonths();
            units = "month(s) ago";
        } else {
            result = period.getYears();
            units = "year(s) ago";
        }

        UserSummary s3 = new UserSummary(SummaryTitle.JOINED, result, units);
        summaryList.add(s3);

        return summaryList;
    }
}
