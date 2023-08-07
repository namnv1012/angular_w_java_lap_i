package com.laptop.shopping.restful;

import com.laptop.shopping.domain.Producer;
import com.laptop.shopping.domain.User;
import com.laptop.shopping.dto.SearchProducer;
import com.laptop.shopping.repository.iRepository.IProducerRepository;
import com.laptop.shopping.repository.iRepository.IProductRepository;
import com.laptop.shopping.domain.Product;
import com.laptop.shopping.service.iService.IProducerService;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class ProducerResource {

    private final Logger log = LoggerFactory.getLogger(ProducerResource.class);

    private static final String ENTITY_NAME = "producer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final com.laptop.shopping.repository.iRepository.IProducerRepository IProducerRepository;

    private final UserService userService;

    private final IProducerService IProducerService;

    private final com.laptop.shopping.repository.iRepository.IProductRepository IProductRepository;

    public ProducerResource(IProducerRepository IProducerRepository, UserService userService, IProducerService IProducerService, IProductRepository IProductRepository) {
        this.IProducerRepository = IProducerRepository;
        this.userService = userService;
        this.IProducerService = IProducerService;
        this.IProductRepository = IProductRepository;
    }

    @PostMapping("/producers")
    public ResponseEntity<Producer> createProducer(@RequestBody Producer producer) throws URISyntaxException {
        Optional<User> userLogin = userService.getUserWithAuthorities();
        if (producer.getId() == null) {
            producer.setCreateDate(Instant.now());
            producer.setCreateName(userLogin.get().getLogin());
        }else{
            Producer oldProducer = IProducerRepository.findById(producer.getId()).get();
            producer.setCreateDate(oldProducer.getCreateDate());
            producer.setCreateName(oldProducer.getCreateName());
            producer.setUpdateDate(Instant.now());
            producer.setUpdateName(userLogin.get().getLogin());
        }
        producer.setStatus(true);
        Producer result = IProducerRepository.save(producer);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/producers")
    public List<Producer> getAllProducers() {
        return IProducerRepository.findAll();
    }

    @GetMapping("/producers/{id}")
    public ResponseEntity<Producer> getProducer(@PathVariable Long id) {
        log.debug("REST request to get Producer : {}", id);
        Optional<Producer> producer = IProducerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(producer);
    }

    @DeleteMapping("/producers/{id}")
    public ResponseEntity<Void> deleteProducer(@PathVariable Long id) {
        log.debug("REST request to delete Producer : {}", id);
        IProducerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/producers/delete")
    public ResponseEntity<?> deleteCategory(@RequestBody Long id) {
        Producer producer = IProducerRepository.findById(id).get();
        List<Product> lstProduct = IProductRepository.getListProductByCategoryId(producer.getCode());
        if(lstProduct.size() != 0){
             producer.setStatus(false);
            producer = IProducerRepository.save(producer);
        }else{
            IProducerRepository.deleteById(id);
        }
        return ResponseEntity.ok().body(producer);
    }

    @PostMapping("/producers/search")
    public ResponseEntity<?> searchProducer(@RequestBody SearchProducer searchProducer,
                                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(value = "page-size", required = false, defaultValue = "10") Integer pageSize){
        Map<String, Object> result = IProducerService.searchProducer(searchProducer, page, pageSize);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/producers/findByCode")
    public ResponseEntity<?> findByCode(@RequestParam(value = "code") String code){
        Producer producer = IProducerRepository.findByCode(code);
        return ResponseEntity.ok().body(producer);
    }

    @GetMapping("/producers/getAllByStatusActive")
    public ResponseEntity<?> getAllByStatusActive(){
        List<Producer> lstProducer = IProducerRepository.getAllByStatusActive();
        return ResponseEntity.ok().body(lstProducer);
    }
}
