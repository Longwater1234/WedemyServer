package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, String> {

    @Query("select s from Sales s where s.userId.id = ?1")
    List<Sales> findByUserId_IdEquals(Integer id);

    List<Sales> findByCreatedAtBetween(Instant createdAtStart, Instant createdAtEnd);

}
