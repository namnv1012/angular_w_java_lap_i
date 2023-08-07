package com.laptop.shopping.restful;

import com.laptop.shopping.domain.OrderItem;
import com.laptop.shopping.repository.iRepository.IOrderItemRepository;
import com.laptop.shopping.repository.iRepository.IOrdersRepository;
import com.laptop.shopping.repository.iRepository.custom.IUserCustomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Transactional
public class DashBoardResource {

    private final com.laptop.shopping.repository.iRepository.IOrdersRepository IOrdersRepository;

    private final com.laptop.shopping.repository.iRepository.custom.IUserCustomRepository IUserCustomRepository;

    private final com.laptop.shopping.repository.iRepository.IOrderItemRepository IOrderItemRepository;

    public DashBoardResource(IOrdersRepository IOrdersRepository, IUserCustomRepository IUserCustomRepository, IOrderItemRepository IOrderItemRepository) {
        this.IOrdersRepository = IOrdersRepository;
        this.IUserCustomRepository = IUserCustomRepository;
        this.IOrderItemRepository = IOrderItemRepository;
    }

    @GetMapping("/load-home")
    public ResponseEntity<?> loadHome(){
        Map<String, Object> result = new HashMap<>();
        Integer totalOrder = IOrdersRepository.getListOrderByStatus(1).size();
        Integer totalCustomer = IUserCustomRepository.getListUserByRole("ROLE_CUSTOMER").size();
        Integer totalEmployee = IUserCustomRepository.getListUserByRole("ROLE_USER").size();
        List<OrderItem> lstOrderItem = IOrderItemRepository.findAll();
        float totalPrice = 0L;
        for (OrderItem orderItem: lstOrderItem){
            totalPrice += (orderItem.getQuantity() * orderItem.getPrice());
        }
        result.put("totalOrder", totalOrder);
        result.put("totalCustomer", totalCustomer);
        result.put("totalEmployee", totalEmployee);
        result.put("totalPrice", totalPrice);
        return ResponseEntity.ok().body(result);
    }
}
