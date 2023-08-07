package com.laptop.shopping.restful;

import com.laptop.shopping.domain.OrderItem;
import com.laptop.shopping.dto.OrderItemDto;
import com.laptop.shopping.repository.iRepository.IOrderItemRepository;
import com.laptop.shopping.service.iService.IOrderItemService;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class OrderItemResource {

    private final Logger log = LoggerFactory.getLogger(OrderItemResource.class);

    private static final String ENTITY_NAME = "orderItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final com.laptop.shopping.repository.iRepository.IOrderItemRepository IOrderItemRepository;

    private final IOrderItemService IOrderItemService;

    public OrderItemResource(IOrderItemRepository IOrderItemRepository, IOrderItemService IOrderItemService) {
        this.IOrderItemRepository = IOrderItemRepository;
        this.IOrderItemService = IOrderItemService;
    }

    @PostMapping("/order-items")
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) throws URISyntaxException {
        OrderItem result = IOrderItemRepository.save(orderItem);
        return ResponseEntity.ok().body(result);
    }



    @GetMapping("/order-items")
    public List<OrderItem> getAllOrderItems() {
        log.debug("REST request to get all OrderItems");
        return IOrderItemRepository.findAll();
    }

    @GetMapping("/order-items/{id}")
    public ResponseEntity<List<OrderItemDto>> getOrderItem(@PathVariable Long id) {
        List<OrderItemDto> lstOrderItem = IOrderItemService.getListOrderItemByOrderId(id);
        return ResponseEntity.ok().body(lstOrderItem);
    }

    @DeleteMapping("/order-items/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete OrderItem : {}", id);
        IOrderItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<OrderItemDto>> getProductSelling(){
        List<OrderItemDto> listOderSelling = IOrderItemService.getAllOderItem();
        return ResponseEntity.ok().body(listOderSelling);
    }
}
