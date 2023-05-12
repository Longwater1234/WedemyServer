package com.davistiba.wedemyserver.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @RepeatedTest(2)
    void checkIfExistWishlistNativeTest() {
        long exists = wishlistRepository.checkIfExistWishlistNative(5, 10010);
        long exists2 = wishlistRepository.checkIfExistWishlistNative(1, 10015);
        Assertions.assertEquals(exists, exists2);
    }

    @RepeatedTest(2)
    void checkIfCourseInWishlistTest() {
        boolean exists = wishlistRepository.checkIfCourseInWishlist(5, 10010);
        boolean exists2 = wishlistRepository.checkIfCourseInWishlist(1, 10015);
        Assertions.assertEquals(exists, exists2);
    }

}