package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> getByUserId_IdOrderByCreatedAtDesc(Integer id);

    List<OrderItems> findByCreatedAtBetween(Instant createdAtStart, Instant createdAtEnd);


}
