package com.davistiba.wedemyserver.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Test
    @Order(1)
    public void contextLoads() {

    }

    @RepeatedTest(3)
    void checkIfExistWishlistNative_Test() {
        boolean exists = wishlistRepository.checkIfExistWishlistNative(8, 10011);
        Assertions.assertTrue(exists);
    }

}