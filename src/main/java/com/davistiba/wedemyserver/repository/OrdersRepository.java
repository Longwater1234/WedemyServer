package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<OrderItem, Long> {


}
