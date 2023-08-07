package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.OrderItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(value = "SELECT * FROM order_item o WHERE o.order_id = ?1", nativeQuery = true)
    List<OrderItem> findByOrderId(Long id);
}
