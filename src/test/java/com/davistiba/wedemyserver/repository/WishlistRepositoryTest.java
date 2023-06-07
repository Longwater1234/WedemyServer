package com.davistiba.wedemyserver.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @RepeatedTest(5)
    void checkIfExistWishlistNativeTest() {
        int exists = wishlistRepository.checkIfExistWishlistNative(9, 10015);
        Assertions.assertEquals(1, exists);
    }

    @RepeatedTest(5)
    void checkIfCourseInWishlistTest() {
        boolean exists = wishlistRepository.checkIfCourseInWishlist(9, 10015);
        Assertions.assertTrue(exists);
    }

}