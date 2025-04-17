package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.CheckoutRequest;
import com.davistiba.wedemyserver.dto.MyCustomResponse;
import com.davistiba.wedemyserver.models.*;
import com.davistiba.wedemyserver.repository.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Process all courses in Cart.
     * Save batch as single Sale.
     * Insert each item to OrderItems.
     * Then insert each distinct item to Enrollment table.
     * Finally, clear Cart of current user.
     *
     * @param transactionId from Braintree, after payment success
     * @param request       from frontend.
     * @param user          the current student
     */
    @Transactional
    public MyCustomResponse processCheckoutDatabase(final String transactionId,
                                                    @NotNull CheckoutRequest request,
                                                    final User user) {
        Page<Course> coursePage = courseRepository.getCartListByUser(user.getId(), Pageable.unpaged());

        //===== begin DB OPERATIONS ========
        Sales savedSale = salesRepository.save(
                new Sales(transactionId, user, request.getTotalAmount(), request.getPaymentMethod()));

        ArrayList<OrderItem> orderItemList = new ArrayList<>(coursePage.getTotalPages());
        ArrayList<Enrollment> enrollments = new ArrayList<>(coursePage.getTotalPages());

        coursePage.get().forEach(course -> {
            OrderItem o = new OrderItem(savedSale, course);
            orderItemList.add(o);

            Enrollment e = new Enrollment(user, course);
            enrollments.add(e);
        });

        Set<Integer> courseIds = coursePage.get().map(Course::getId).collect(Collectors.toSet());
        orderItemRepository.saveAll(orderItemList);
        enrollmentRepository.batchInsert(enrollments, this.jdbcTemplate);
        cartRepository.deleteByUserIdAndCoursesIn(user.getId(), courseIds);
        wishlistRepository.deleteByUserIdAndCoursesIn(user.getId(), courseIds);
        //-----------------------------------------------
        return new MyCustomResponse("Successfully paid USD " + request.getTotalAmount());
    }
}

