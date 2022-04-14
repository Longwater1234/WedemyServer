package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.dto.UserSummary;
import com.davistiba.wedemyserver.models.SummaryTitle;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import com.davistiba.wedemyserver.repository.UserRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {

    private final UserRepository userRepository;

    private final EnrollmentRepository enrollmentRepository;

    public final Logger logger = LoggerFactory.getLogger(String.valueOf(this));

    @Autowired
    public ProfileController(UserRepository userRepository, EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping(path = "/mine")
    public UserDTO getUserById(@NotNull HttpSession session) {
        try {
            Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
            return userRepository.findUserDTObyId(userId).orElseThrow();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping(path = "/summary")
    @ResponseStatus(value = HttpStatus.OK)
    public List<UserSummary> getUserSummary(@NotNull HttpSession session) {
        User user = MyUserDetailsService.getSessionUserDetails(session);
        List<UserSummary> summaryList = new ArrayList<>();

        long owned = enrollmentRepository.countEnrollmentByUser(user);
        UserSummary s1 = new UserSummary(SummaryTitle.OWNING, owned, "courses");
        summaryList.add(s1);

        long completed = enrollmentRepository.countEnrollmentByUserAndIsCompleted(user, true);
        UserSummary s2 = new UserSummary(SummaryTitle.COMPLETED, completed, "courses");
        summaryList.add(s2);

        Instant dateCreated = userRepository.findById(user.getId()).get().getCreatedAt();

        Period period = Period.between(LocalDate.ofInstant(dateCreated, ZoneId.of("UTC")), LocalDate.now());
        int numberDays = Math.abs(period.getDays()); //get positive
        long result = 0;
        String units;
        if (numberDays == 0) units = "today";
        else if (numberDays > 0 && numberDays <= 30) {
            result = numberDays;
            units = "days ago";
        } else if (numberDays > 30 && numberDays <= 365) {
            result = period.getMonths();
            units = "months ago";
        } else {
            result = period.getYears();
            units = "years ago";
        }

        UserSummary s3 = new UserSummary(SummaryTitle.JOINED, result, units);
        summaryList.add(s3);

        return summaryList;
    }

}
