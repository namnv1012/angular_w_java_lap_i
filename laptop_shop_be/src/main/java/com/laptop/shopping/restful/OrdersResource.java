package com.laptop.shopping.restful;

import com.laptop.shopping.domain.Orders;
import com.laptop.shopping.dto.OrderDto;
import com.laptop.shopping.dto.SearchCategory;
import com.laptop.shopping.dto.ServiceResult;
import com.laptop.shopping.exception.restful.BadRequestAlertException;
import com.laptop.shopping.repository.iRepository.IOrdersRepository;
import com.laptop.shopping.service.iService.IOrderService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class OrdersResource {

    private final Logger log = LoggerFactory.getLogger(OrdersResource.class);

    private static final String ENTITY_NAME = "orders";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final com.laptop.shopping.repository.iRepository.IOrdersRepository IOrdersRepository;

    private final IOrderService IOrderService;

    public OrdersResource(IOrdersRepository IOrdersRepository, IOrderService IOrderService) {
        this.IOrdersRepository = IOrdersRepository;
        this.IOrderService = IOrderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<Orders> createOrders(@RequestBody Orders orders) throws URISyntaxException {
        log.debug("REST request to save Orders : {}", orders);
        if (orders.getId() != null) {
            throw new BadRequestAlertException("A new orders cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Orders result = IOrdersRepository.save(orders);
        return ResponseEntity
            .created(new URI("/api/orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/orders")
    public List<Orders> getAllOrders() {
        log.debug("REST request to get all Orders");
        return IOrdersRepository.findAll();
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Orders> getOrders(@PathVariable Long id) {
        log.debug("REST request to get Orders : {}", id);
        Optional<Orders> orders = IOrdersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orders);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrders(@PathVariable Long id) {
        log.debug("REST request to delete Orders : {}", id);
        IOrdersRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/orders/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrderDto orderDTO){
        Orders orders = IOrderService.checkOut(orderDTO);
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/orders/search")
    public ResponseEntity<?> searchOrders(@RequestBody SearchCategory searchCategory,
                                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(value = "page-size", required = false, defaultValue = "10") Integer pageSize){
        Map<String, Object> result = IOrderService.search(searchCategory, page, pageSize);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/orders/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody Orders orders){
//        Optional<Orders> o = ordersRepository.findById(orders.getId());
//        if(o.isPresent()){
//            Orders oldOrder = o.get();
        ;
//            oldOrder.setStatus(1);
//            orders = ordersRepository.save(oldOrder);
//        }
        ServiceResult<Orders> result = IOrderService.updateStatus(orders);
        return ResponseEntity.ok().body(result);
    }
}
