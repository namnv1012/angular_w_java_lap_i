package com.laptop.shopping.restful;

import com.laptop.shopping.domain.Producer;
import com.laptop.shopping.dto.SearchProducer;
import com.laptop.shopping.repository.iRepository.IProducerRepository;
import com.laptop.shopping.repository.iRepository.IProductRepository;
import com.laptop.shopping.domain.Product;
import com.laptop.shopping.service.iService.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final IProductService IProductService;

    private final com.laptop.shopping.repository.iRepository.IProductRepository IProductRepository;

    private final com.laptop.shopping.repository.iRepository.IProducerRepository IProducerRepository;

    public CustomerResource(IProductService IProductService, IProductRepository IProductRepository, IProducerRepository IProducerRepository) {
        this.IProductService = IProductService;
        this.IProductRepository = IProductRepository;
        this.IProducerRepository = IProducerRepository;
    }

    // APi trang chủ page
    @GetMapping("/customer/home")
    public ResponseEntity<?> loadDataHome(){
        Map<String, Object> result = IProductService.loadDataHome();
        return ResponseEntity.ok().body(result);
    }

    // Chi tiết phone
    @GetMapping("/customer/phone-detail/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long id){
        Product product = IProductRepository.getProductById(id);
        return ResponseEntity.ok().body(product);
    }


    // Danh sách sản phẩm
    @GetMapping("/customer/list-product")
    public ResponseEntity<?> getListProduct(){
        List<Product> lst = IProductRepository.getListProductCustomer();
        return ResponseEntity.ok().body(lst);
    }

    // DS hãng
    @GetMapping("/customer/list-producer")
    public ResponseEntity<?> getAllProducer(){
        List<Producer> lstProducer = IProducerRepository.findAll();
        return ResponseEntity.ok().body(lstProducer);
    }

    // Ds sản phẩm tương tự theo hãng
    @GetMapping("/customer/find-by-producer/{id}")
    public ResponseEntity<?> findByProducer(@PathVariable String id){
        List<Product> lstProduct = IProductRepository.getListProductByProducerCode(id);
        return ResponseEntity.ok().body(lstProduct);
    }

    @GetMapping("/customer/find-product-by-producerCode/{producerCode}")
    public ResponseEntity<?> findByProducerCode(@PathVariable String producerCode){
        List<Product> lstProduct = IProductRepository.findByProducerCode(producerCode);
        return ResponseEntity.ok().body(lstProduct);
    }
}
