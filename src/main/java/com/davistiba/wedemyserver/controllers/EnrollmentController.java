package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/enroll")
@CrossOrigin
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;


    @GetMapping(path = "/status/c/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkEnrollStatus(@PathVariable @NotNull Integer courseId, HttpSession session) {

        Map<String, Boolean> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        boolean isOwned = enrollmentRepository.existsByCourseIdAndUserId(userId, courseId);
        response.put("isOwned", isOwned);
        return response;

    }


}
