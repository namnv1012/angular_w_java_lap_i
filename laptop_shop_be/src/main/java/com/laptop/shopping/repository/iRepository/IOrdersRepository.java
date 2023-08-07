package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.Orders;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrdersRepository extends JpaRepository<Orders, Long> {
    @Query(value = "SELECT * FROM orders o WHERE o.status = ?1", nativeQuery = true)
    List<Orders> getListOrderByStatus(Integer status);
}
