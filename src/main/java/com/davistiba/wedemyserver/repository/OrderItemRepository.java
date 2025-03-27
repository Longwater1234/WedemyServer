package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.OrderItemDTO;
import com.davistiba.wedemyserver.models.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {

    @Query("SELECT new com.davistiba.wedemyserver.dto.OrderItemDTO(o.id, c.title, c.price) from OrderItem o " +
            "INNER JOIN Course c on o.course.id = c.id where o.sale.transactionId = ?1")
    Slice<OrderItemDTO> findByTransactionIdEquals(String transactionId, Pageable pageable);

    @Transactional
    default void batchInsert(@NotEmpty List<OrderItem> orderItemList, final JdbcTemplate jdbcTemplate) {
        jdbcTemplate.batchUpdate("INSERT INTO order_items (course_id, transaction_id) VALUES (?, ?)", orderItemList,
                100, (ps, orderItem) -> {
                    int col = 1;
                    ps.setInt(col++, orderItem.getCourse().getId());
                    ps.setString(col++, orderItem.getSale().getTransactionId());
                });
    }

}

