package com.laptop.shopping.restful;

import com.laptop.shopping.common.FileUtil;
import com.laptop.shopping.domain.User;
import com.laptop.shopping.dto.ProductDto;
import com.laptop.shopping.dto.SearchProducer;
import com.laptop.shopping.dto.ServiceResult;
import com.laptop.shopping.repository.iRepository.IProductRepository;
import com.laptop.shopping.domain.Product;
import com.laptop.shopping.service.iService.IProductService;
import com.laptop.shopping.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final com.laptop.shopping.repository.iRepository.IProductRepository IProductRepository;

    private final UserService userService;

    @Value("${save-image.path-img}")
    private String folderImage;

    private final IProductService IProductService;

    public ProductResource(IProductRepository IProductRepository, UserService userService, IProductService IProductService) {
        this.IProductRepository = IProductRepository;
        this.userService = userService;
        this.IProductService = IProductService;
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestPart("product") Product product,
                                                 @RequestPart(value = "fileImage", required = false) MultipartFile fileImageProduct) throws URISyntaxException, IOException {
        log.debug("REST request to save Product : {}", product);
        Optional<User> userLogin = userService.getUserWithAuthorities();
        if(fileImageProduct != null){
            String nameFile = fileImageProduct.getOriginalFilename();
            String fileInputPath =  this.folderImage  + nameFile;
            FileUtil.saveFile(
                fileInputPath, fileImageProduct
            );
            product.setImageUrl(FileUtil.formatPathImg(fileInputPath));
        }
        if (product.getId() == null) {
            product.setCreateDate(Instant.now());
            product.setCreateName(userLogin.get().getLogin());
        }else{
            Product oldProduct = IProductRepository.findById(product.getId()).get();
            product.setCreateDate(oldProduct.getCreateDate());
            product.setCreateName(oldProduct.getCreateName());
            product.setUpdateDate(Instant.now());
            product.setUpdateName(userLogin.get().getLogin());
        }
        product.setStatus(true);
        Product result = IProductRepository.save(product);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        log.debug("REST request to get all Products");
        return IProductRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<Product> product = IProductRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(product);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        IProductRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/products/saveProduct")
    public ResponseEntity<?> saveProduct(@RequestPart("productDTO") ProductDto productDTO,
                                         @RequestPart(value = "imgProduct1", required = false) MultipartFile fileImageProduct,
                                         @RequestPart(value = "imgProduct2", required = false) MultipartFile fileImageProduct2,
                                         @RequestPart(value = "imgProduct3", required = false) MultipartFile fileImageProduct3) throws IOException {
        ServiceResult<Product> result = IProductService.save(productDTO, fileImageProduct, fileImageProduct2, fileImageProduct3);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/products/findById/{id}")
    public ResponseEntity<ProductDto> findByProductId(@PathVariable Long id){
        ProductDto productDTO = IProductService.findByProductId(id);
        return ResponseEntity.ok().body(productDTO);
    }

    @PostMapping("/products/search")
    public ResponseEntity<?> searchProducer(@RequestBody SearchProducer searchProducer,
                                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(value = "page-size", required = false, defaultValue = "10") Integer pageSize){
        Map<String, Object> result = IProductService.search(searchProducer, page, pageSize);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/products/import-quantity")
    public ResponseEntity<?> importQuantity(@RequestBody ProductDto productDTO){
        Product product = IProductService.importQuantity(productDTO);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/products/findByCode")
    public ResponseEntity<Product> findByCode(@RequestParam(value = "code") String code){
        Product product = IProductRepository.findByCode(code);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/get-products-inventory")
    public ResponseEntity<List<ProductDto>> getAllProductInventory(){
        List<ProductDto> productList = IProductService.getAllProductInventory();
        return ResponseEntity.ok().body(productList);
    }
}
