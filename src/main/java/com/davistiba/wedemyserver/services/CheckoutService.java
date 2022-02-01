package com.davistiba.wedemyserver.services;

import com.davistiba.wedemyserver.dto.CheckoutRequest;
import com.davistiba.wedemyserver.models.*;
import com.davistiba.wedemyserver.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckoutService {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    SalesRepository salesRepository;

    public final Logger logger = LoggerFactory.getLogger(String.valueOf(this));

    /**
     * Process all items in Cart.
     * Save batch as single Sale.
     * Save each item to OrderItems.
     * Add each course to Enrollment table.
     * Finally, delete all items from Cart table
     *
     * @param transactionId request
     * @param userId        user session
     * @param request       request body from client
     * @return success or fail
     */
    @Transactional
    public Map<String, Object> processCheckoutDatabase(String transactionId,
                                                       Integer userId,
                                                       CheckoutRequest request,
                                                       User user) {
        logger.info("PROCESS: Now in thread {}", Thread.currentThread().getName());
        Map<String, Object> response = new HashMap<>();

        List<OrderItem> orderItemList = new ArrayList<>();
        List<Course> courses = courseRepository.findCoursesByIdIn(request.getCourses());
        List<Enrollment> enrollments = new ArrayList<>();

        //===== begin DB OPERATIONS ========
        Sales savedSale = salesRepository.saveAndFlush(
                new Sales(transactionId, user, request.getTotalAmount(), request.getPaymentMethod()));

        for (Course course : courses) {
            OrderItem o = new OrderItem(savedSale, course);
            orderItemList.add(o);

            Enrollment e = new Enrollment(user, course);
            enrollments.add(e);
        }

        ordersRepository.saveAllAndFlush(orderItemList);
        long i = cartRepository.deleteByCourseIdAndUserId(request.getCourses(), userId);
        enrollmentRepository.saveAllAndFlush(enrollments);
        logger.info("DELETED COUNT CART {}", i);
        //-----------------------------------------------
        response.put("success", true);
        response.put("message", "Successfully paid " + request.getTotalAmount());
        return response;

    }
}
