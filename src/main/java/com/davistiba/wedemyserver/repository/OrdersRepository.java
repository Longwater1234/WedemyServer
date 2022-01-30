package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> getByUserId_IdOrderByCreatedAtDesc(Integer id);

    List<Orders> findByCreatedAtBetween(Instant createdAtStart, Instant createdAtEnd);


}
