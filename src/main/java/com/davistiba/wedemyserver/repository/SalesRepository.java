package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.SalesDTO;
import com.davistiba.wedemyserver.models.Sales;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Repository
public interface SalesRepository extends CrudRepository<Sales, String> {

    Slice<Sales> findByCreatedAtBetween(Instant createdAtStart, Instant createdAtEnd);

    @Query("SELECT new com.davistiba.wedemyserver.dto.SalesDTO(s.transactionId,s.createdAt, s.paymentMethod, s.totalPaid, count(o)) " +
           "FROM Sales s JOIN OrderItem o ON s.transactionId = o.sale.transactionId WHERE s.user.id = ?1 GROUP BY s.transactionId")
    Slice<SalesDTO> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO sales (transaction_id, user_id, total_paid, payment_method, created_at) " +
                   "VALUES (?1, ?2, ?3, ?4, UTC_TIMESTAMP())", nativeQuery = true)
    int insertNative(String transactionId, Integer userId, BigDecimal totalPaid, String paymentMethod);

}
