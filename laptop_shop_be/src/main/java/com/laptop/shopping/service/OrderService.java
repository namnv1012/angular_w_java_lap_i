package com.laptop.shopping.service;

import com.laptop.shopping.domain.*;
import com.laptop.shopping.dto.OrderDto;
import com.laptop.shopping.dto.OrderItemDto;
import com.laptop.shopping.dto.SearchCategory;
import com.laptop.shopping.dto.ServiceResult;
import com.laptop.shopping.repository.iRepository.IOrderItemRepository;
import com.laptop.shopping.repository.iRepository.IOrdersRepository;
import com.laptop.shopping.repository.iRepository.IProductRepository;
import com.laptop.shopping.repository.iRepository.IWarrantyRepository;
import com.laptop.shopping.repository.iRepository.custom.IOrdersCustomRepository;
import com.laptop.shopping.service.iService.IOrderService;
import com.laptop.shopping.domain.*;
import com.laptop.shopping.repository.iRepository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OrderService implements IOrderService {

    private final com.laptop.shopping.repository.iRepository.IOrdersRepository IOrdersRepository;

    private final com.laptop.shopping.repository.iRepository.IOrderItemRepository IOrderItemRepository;

    private final UserService userService;

    private final com.laptop.shopping.repository.iRepository.IProductRepository IProductRepository;

    private final com.laptop.shopping.repository.iRepository.custom.IOrdersCustomRepository IOrdersCustomRepository;

    private final com.laptop.shopping.repository.iRepository.IWarrantyRepository IWarrantyRepository;

    public OrderService(IOrdersRepository IOrdersRepository, IOrderItemRepository IOrderItemRepository, UserService userService, IProductRepository IProductRepository, IOrdersCustomRepository IOrdersCustomRepository, IWarrantyRepository IWarrantyRepository) {
        this.IOrdersRepository = IOrdersRepository;
        this.IOrderItemRepository = IOrderItemRepository;
        this.userService = userService;
        this.IProductRepository = IProductRepository;
        this.IOrdersCustomRepository = IOrdersCustomRepository;
        this.IWarrantyRepository = IWarrantyRepository;
    }

    @Override
    public Orders checkOut(OrderDto orderDTO) {
        Optional<User> useLogin = userService.getUserWithAuthorities();
        // Lưu bảng order
        Orders orders = new Orders();
        orders.setUserCode(useLogin.get().getLogin());
        orders.setName(orderDTO.getName());
        orders.setPhone(orderDTO.getPhone());
        orders.setAddress(orderDTO.getAddress());
        orders.setEmail(orderDTO.getEmail());
        orders.setNote(orderDTO.getNote());
        orders.setEmail(orderDTO.getEmail());
        orders.setStatus(0);
        orders.setOrderDate(Instant.now());
        orders = IOrdersRepository.save(orders);
        // Lưu bảng orderItem
        List<OrderItemDto> lstOrderItem = orderDTO.getLstOrderItem();
        for(OrderItemDto orderItemDTO: lstOrderItem){
            OrderItem item = new OrderItem();
            item.setOrderId(orders.getId());
            item.setProductCode(orderItemDTO.getProductCode());
            item.setQuantity(orderItemDTO.getQuantity());
            Product product = IProductRepository.findByCode(orderItemDTO.getProductCode());
            item.setPrice(product.getPrice());
            IOrderItemRepository.save(item);
            // Cập nhật số lượng sản phẩm
            Integer quantity = product.getQuantity() - item.getQuantity();
            product.setQuantity(quantity);
            product = IProductRepository.save(product);
        }
        return orders;
    }

    @Override
    public Map<String, Object> search(SearchCategory searchCategory, Integer page, Integer pageSize) {
        List<Orders> lstOrders = IOrdersCustomRepository.search(searchCategory, page, pageSize);
        Integer total = IOrdersCustomRepository.totalOrder(searchCategory).size();
        Map<String, Object> result = new HashMap<>();
        result.put("lstOrders", lstOrders);
        result.put("total", total);
        return result;
    }

    @Override
    public ServiceResult<Orders> updateStatus(Orders orders) {
        ServiceResult<Orders> result = new ServiceResult<>();
        try{
            Optional<User> useLogin = userService.getUserWithAuthorities();
            Optional<Orders> o = IOrdersRepository.findById(orders.getId());
            if(o.isPresent()){
                Orders oldOrder = o.get();
                oldOrder.setStatus(1);
                orders = IOrdersRepository.save(oldOrder);
            }
            List<OrderItem> lstOrderItem = IOrderItemRepository.findByOrderId(orders.getId());
            for(OrderItem orderItem: lstOrderItem){
                Warranty warranty = new Warranty();
                warranty.setOrderId(orders.getId());
                warranty.setCustomerName(orders.getUserCode());
                warranty.setPhoneNumber(orders.getPhone());
                warranty.setCreateDate(Instant.now());
                warranty.setCreateName(useLogin.get().getLogin());
                // Tính tg hết hạn bảo hành
                Product product = IProductRepository.findByCode(orderItem.getProductCode());
                Instant dateGuarantee = Instant.now().plus(product.getGuarantee() * 30, ChronoUnit.DAYS);
                warranty.setExpirationDate(dateGuarantee);
                IWarrantyRepository.save(warranty);
            }
            result.setMessage("Thành công");
            result.setStatus(HttpStatus.OK);
            result.setData(orders);
        }catch (Exception ex) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("Thất bại");
        }
        return result;
    }
}
