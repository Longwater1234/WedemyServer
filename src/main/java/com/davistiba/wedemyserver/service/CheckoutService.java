package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.CheckoutRequest;
import com.davistiba.wedemyserver.models.*;
import com.davistiba.wedemyserver.repository.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SalesRepository salesRepository;

    /**
     * Process all courses in Cart.
     * Save batch as single Sale.
     * Insert each item to OrderItems.
     * Then insert each distinct item to Enrollment table.
     * Finally, clear Cart of current user.
     */
    @Transactional
    public MyCustomResponse processCheckoutDatabase(String transactionId,
                                                    @NotNull CheckoutRequest request,
                                                    User user) {

        List<OrderItem> orderItemList = new ArrayList<>();
        List<Enrollment> enrollments = new ArrayList<>();
        Page<Course> coursePage = courseRepository.getCoursesCartByUser(user.getId(), Pageable.unpaged());

        //===== begin DB OPERATIONS ========
        Sales savedSale = salesRepository.save(
                new Sales(transactionId, user, request.getTotalAmount(), request.getPaymentMethod()));

        coursePage.get().forEach(course -> {
            OrderItem o = new OrderItem(savedSale, course);
            orderItemList.add(o);

            Enrollment e = new Enrollment(user, course);
            enrollments.add(e);
        });

        List<Integer> courseIds = coursePage.get().map(Course::getId).collect(Collectors.toUnmodifiableList());

        orderItemRepository.saveAll(orderItemList);
        enrollmentRepository.saveAll(enrollments);
        cartRepository.deleteByUserIdAndCoursesIn(user.getId(), courseIds);
        wishlistRepository.deleteByUserIdAndCoursesIn(user.getId(), courseIds);
        //-----------------------------------------------
        return new MyCustomResponse("Successfully paid USD " + request.getTotalAmount());
    }
}
