package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.CheckoutRequest;
import com.davistiba.wedemyserver.models.*;
import com.davistiba.wedemyserver.repository.*;
import org.jetbrains.annotations.NotNull;
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
    private OrdersRepository ordersRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SalesRepository salesRepository;

    /**
     * Process all items in Cart.
     * Save batch as single Sale.
     * Insert each item to OrderItems.
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
                                                       @NotNull CheckoutRequest request,
                                                       User user) {

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
        cartRepository.deleteByCourseIdAndUserId(request.getCourses(), userId);
        enrollmentRepository.saveAllAndFlush(enrollments);
        //-----------------------------------------------
        response.put("success", true);
        response.put("message", "Successfully paid USD " + request.getTotalAmount());
        return response;

    }
}
