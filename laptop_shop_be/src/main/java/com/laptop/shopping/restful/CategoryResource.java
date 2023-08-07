package com.laptop.shopping.restful;

import com.laptop.shopping.domain.Category;
import com.laptop.shopping.domain.Product;
import com.laptop.shopping.domain.User;
import com.laptop.shopping.dto.SearchCategory;
import com.laptop.shopping.repository.iRepository.ICategoryRepository;
import com.laptop.shopping.repository.iRepository.IProductRepository;
import com.laptop.shopping.service.iService.ICategoryService;
import com.laptop.shopping.service.UserService;

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
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private static final String ENTITY_NAME = "category";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final com.laptop.shopping.repository.iRepository.ICategoryRepository ICategoryRepository;

    private final UserService userService;

    private final ICategoryService ICategoryService;

    private final com.laptop.shopping.repository.iRepository.IProductRepository IProductRepository;

    public CategoryResource(ICategoryRepository ICategoryRepository, UserService userService, ICategoryService ICategoryService, IProductRepository IProductRepository) {
        this.ICategoryRepository = ICategoryRepository;
        this.userService = userService;
        this.ICategoryService = ICategoryService;
        this.IProductRepository = IProductRepository;
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) throws URISyntaxException {
        Optional<User> userLogin = userService.getUserWithAuthorities();
        if (category.getId() == null) {
            category.setCreateDate(Instant.now());
            category.setCreateName(userLogin.get().getLogin());
        }else{
            Category oldCategory = ICategoryRepository.findById(category.getId()).get();
            category.setCreateDate(oldCategory.getCreateDate());
            category.setCreateName(oldCategory.getCreateName());
            category.setUpdateDate(Instant.now());
            category.setUpdateName(userLogin.get().getLogin());
        }
        category.setStatus(true);
        Category result = ICategoryRepository.save(category);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        log.debug("REST request to get all Categories");
        return ICategoryRepository.findAll();
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        Optional<Category> category = ICategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(category);
    }

    @PostMapping("/categories/delete")
    public ResponseEntity<?> deleteCategory(@RequestBody Long id) {
        Category category = ICategoryRepository.findById(id).get();
        List<Product> lstProduct = IProductRepository.getListProductByCategoryId(category.getCode());
        if(lstProduct.size() != 0){
            category.setStatus(false);
            category = ICategoryRepository.save(category);
        }else{
            ICategoryRepository.deleteById(id);
        }
        return ResponseEntity.ok().body(category);
    }

    @PostMapping("/categories/search")
    public ResponseEntity<?> searchCategory(@RequestBody SearchCategory searchCategory,
                                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(value = "page-size", required = false, defaultValue = "10") Integer pageSize){
        Map<String, Object> result = ICategoryService.search(searchCategory, page, pageSize);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/categories/findByCode")
    public ResponseEntity<?> findByCode(@RequestParam(value = "code")String categoryCode){
        Category category = ICategoryRepository.findByCode(categoryCode);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/categories/getAllByStatusActive")
    public ResponseEntity<?> getAllByStatusActive(){
        List<Category> lstCategory = ICategoryRepository.getAllStatusActive();
        return ResponseEntity.ok().body(lstCategory);
    }
}
