package com.laptop.shopping.service.iService;

import com.laptop.shopping.dto.OrderItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderItemService {
    List<OrderItemDto> getListOrderItemByOrderId(Long orderId);
    List<OrderItemDto> getAllOderItem();
}
