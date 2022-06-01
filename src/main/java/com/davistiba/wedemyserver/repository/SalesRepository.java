package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.SalesDTO;
import com.davistiba.wedemyserver.models.Sales;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, String> {

    @Query("SELECT s FROM Sales s WHERE s.user.id = ?1")
    List<Sales> findByUserId_IdEquals(Integer id);

    List<Sales> findByCreatedAtBetween(Instant createdAtStart, Instant createdAtEnd);

    @Query("SELECT new com.davistiba.wedemyserver.dto.SalesDTO(s.transactionId,s.createdAt, s.paymentMethod, s.totalPaid, count(o)) " +
            "FROM Sales s JOIN OrderItem o ON s.transactionId = o.sale.transactionId WHERE s.user.id = ?1 GROUP BY s.transactionId " +
            "ORDER BY s.createdAt DESC")
    List<SalesDTO> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);


}
