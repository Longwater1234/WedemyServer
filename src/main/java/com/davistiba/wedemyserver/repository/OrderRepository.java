package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Orders, String> {
    @Query("select o from Orders o where o.userId.id = ?1")
    List<Orders> findByUserId_Id(Integer id);

}
