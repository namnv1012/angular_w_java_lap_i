package com.laptop.shopping.service;

import com.laptop.shopping.dto.OrderItemDto;
import com.laptop.shopping.repository.iRepository.custom.IOrderItemCustomRepository;
import com.laptop.shopping.service.iService.IOrderItemService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemService implements IOrderItemService {

    private final com.laptop.shopping.repository.iRepository.custom.IOrderItemCustomRepository IOrderItemCustomRepository;

    public OrderItemService(IOrderItemCustomRepository IOrderItemCustomRepository) {
        this.IOrderItemCustomRepository = IOrderItemCustomRepository;
    }

    @Override
    public List<OrderItemDto> getListOrderItemByOrderId(Long orderId) {
        return IOrderItemCustomRepository.getListOrderItemByOrderId(orderId);
    }

    @Override
    public List<OrderItemDto> getAllOderItem() {
        return IOrderItemCustomRepository.getAllOrder();
    }
}
