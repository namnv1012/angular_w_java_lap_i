package com.laptop.shopping.repository.iRepository.custom;

import com.laptop.shopping.dto.OrderItemDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderItemCustomRepository {
    List<OrderItemDto> getListOrderItemByOrderId(Long orderId);
    List<OrderItemDto> getAllOrder();
}
