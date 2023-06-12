package com.davistiba.wedemyserver.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Test
    void checkIfExistWishlistNativeTest() {
        int exists = wishlistRepository.checkIfExistWishlistNative(9, 10011);
        Assertions.assertEquals(1, exists);
    }

    @Test
    void checkIfCourseInWishlistTest() {
        boolean exists = wishlistRepository.checkIfCourseInWishlist(9, 10011);
        Assertions.assertTrue(exists);
    }

}