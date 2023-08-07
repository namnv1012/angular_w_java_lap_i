package com.laptop.shopping.restful;

import com.laptop.shopping.domain.Warranty;
import com.laptop.shopping.exception.restful.BadRequestAlertException;
import com.laptop.shopping.repository.iRepository.IProductRepository;
import com.laptop.shopping.repository.iRepository.IWarrantyRepository;
import com.laptop.shopping.domain.Product;

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

/**
 * REST controller for managing {@link Warranty}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WarrantyResource {

    private final Logger log = LoggerFactory.getLogger(WarrantyResource.class);

    private static final String ENTITY_NAME = "warranty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final com.laptop.shopping.repository.iRepository.IWarrantyRepository IWarrantyRepository;

    private final com.laptop.shopping.repository.iRepository.IProductRepository IProductRepository;

    public WarrantyResource(IWarrantyRepository IWarrantyRepository, IProductRepository IProductRepository) {
        this.IWarrantyRepository = IWarrantyRepository;
        this.IProductRepository = IProductRepository;
    }

    @PostMapping("/warranties")
    public ResponseEntity<Warranty> createWarranty(@RequestBody Warranty warranty) throws URISyntaxException {
        log.debug("REST request to save Warranty : {}", warranty);
        if (warranty.getId() != null) {
            throw new BadRequestAlertException("A new warranty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Warranty result = IWarrantyRepository.save(warranty);
        return ResponseEntity
            .created(new URI("/api/warranties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/warranties")
    public List<Warranty> getAllWarranties() {
        log.debug("REST request to get all Warranties");
        return IWarrantyRepository.findAll();
    }

    @GetMapping("/warranties/{id}")
    public ResponseEntity<Warranty> getWarranty(@PathVariable Long id) {
        log.debug("REST request to get Warranty : {}", id);
        Optional<Warranty> warranty = IWarrantyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(warranty);
    }

    @GetMapping("/warranties/get-list-product/{id}")
    public ResponseEntity<?> getListProductByWarrantyId(@PathVariable Long id){
        List<Product> result = IProductRepository.findByWarrantyId(id);
        return ResponseEntity.ok().body(result);
    }
}
