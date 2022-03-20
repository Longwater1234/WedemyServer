package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.OrderItemDTO;
import com.davistiba.wedemyserver.models.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT new com.davistiba.wedemyserver.dto.OrderItemDTO(o.id, c.title, c.price) from OrderItem o " +
            "JOIN Course c on o.course.id = c.id where o.sale.transactionId = ?1")
    List<OrderItemDTO> findByTransactionIdEquals(String transactionId, Pageable pageable);


}
