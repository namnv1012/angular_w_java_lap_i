package com.laptop.shopping.restful;

import com.laptop.shopping.domain.Cart;
import com.laptop.shopping.dto.CartDto;
import com.laptop.shopping.repository.iRepository.ICartRepository;
import com.laptop.shopping.service.iService.ICartService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
public class CartResource {
    private final Logger log = LoggerFactory.getLogger(CartResource.class);
    private static final String ENTITY_NAME = "cart";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final com.laptop.shopping.repository.iRepository.ICartRepository ICartRepository;
    private final ICartService ICartService;

    public CartResource(ICartRepository ICartRepository, ICartService ICartService) {
        this.ICartRepository = ICartRepository;
        this.ICartService = ICartService;
    }

    @PostMapping("/carts")
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) throws URISyntaxException {
        Cart result = ICartRepository.save(cart);
        return ResponseEntity
            .created(new URI("/api/carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/carts")
    public ResponseEntity<?> getAllCarts() {
        log.debug("REST request to get all Carts");
        List<CartDto> lst = ICartService.getListByUerLogin();
        return ResponseEntity.ok().body(lst);
    }

    @GetMapping("/carts/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable Long id) {
        log.debug("REST request to get Cart : {}", id);
        Optional<Cart> cart = ICartRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cart);
    }

    @PostMapping("/carts/delete/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        Integer i = null;
        try {
            ICartRepository.deleteById(id);
            i = 1;

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return ResponseEntity.ok().body(i);
    }


}
