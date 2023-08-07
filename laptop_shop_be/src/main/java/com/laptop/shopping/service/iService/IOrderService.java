package com.laptop.shopping.service.iService;

import com.laptop.shopping.domain.Orders;
import com.laptop.shopping.dto.OrderDto;
import com.laptop.shopping.dto.SearchCategory;
import com.laptop.shopping.dto.ServiceResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IOrderService {
    Orders checkOut(OrderDto orderDTO);
    Map<String, Object> search(SearchCategory searchCategory, Integer page, Integer pageSize);
    ServiceResult<Orders> updateStatus(Orders orders);
}
