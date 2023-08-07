package com.laptop.shopping.restful;

import com.laptop.shopping.domain.Product;
import com.laptop.shopping.repository.iRepository.IProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/passers")
public class PassersResource {

    private final com.laptop.shopping.repository.iRepository.IProductRepository IProductRepository;

    public PassersResource(IProductRepository IProductRepository) {
        this.IProductRepository = IProductRepository;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> findByProductId(@PathVariable Long id){
        Product product = IProductRepository.getProductById(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/product/getAll")
    public ResponseEntity<List<Product>> getAll(){
        List<Product> lst = IProductRepository.findAll();
        return ResponseEntity.ok().body(lst);
    }

    @GetMapping("/product/search")
    public ResponseEntity<?> search(@RequestParam(value = "name") String name){
        List<Product> lstProduct = new ArrayList<>();
        if(name.isBlank()){
            lstProduct = IProductRepository.findAll();
        }else{
         lstProduct = IProductRepository.search(name);
        }
        return ResponseEntity.ok().body(lstProduct);
    }

}
