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
    private OrderItemRepository orderItemRepository;

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
     * Then add each item to Enrollment table.
     * Finally, delete all items from Cart table by this user
     *
     * @param transactionId request
     * @param user          the customer
     * @param request       request body from client
     * @return success or fail
     */
    @Transactional
    public Map<String, Object> processCheckoutDatabase(String transactionId,
                                                       @NotNull CheckoutRequest request,
                                                       User user) {

        Map<String, Object> response = new HashMap<>();

        //TODO use db triggers for Cart cleanup
        List<OrderItem> orderItemList = new ArrayList<>();
        List<Course> courseList = courseRepository.findCoursesByIdIn(request.getCourses());
        List<Enrollment> enrollments = new ArrayList<>();

        //===== begin DB OPERATIONS ========
        Sales savedSale = salesRepository.saveAndFlush(
                new Sales(transactionId, user, request.getTotalAmount(), request.getPaymentMethod()));

        for (Course course : courseList) {
            OrderItem o = new OrderItem(savedSale, course);
            orderItemList.add(o);

            Enrollment e = new Enrollment(user, course);
            enrollments.add(e);
        }

        orderItemRepository.saveAllAndFlush(orderItemList);
        cartRepository.deleteAllByUserIdAndCourseIdIn(user.getId(), request.getCourses());
        enrollmentRepository.saveAllAndFlush(enrollments);
        //-----------------------------------------------
        response.put("success", true);
        response.put("message", "Successfully paid USD " + request.getTotalAmount());
        return response;

    }
}
