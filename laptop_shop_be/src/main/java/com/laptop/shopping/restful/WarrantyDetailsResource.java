package com.laptop.shopping.restful;

import com.laptop.shopping.domain.WarrantyDetails;
import com.laptop.shopping.dto.WarrantyDetailsDto;
import com.laptop.shopping.repository.iRepository.IWarrantyDetailsRepository;
import com.laptop.shopping.service.iService.IWarrantyDetailsService;

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
public class WarrantyDetailsResource {

    private final Logger log = LoggerFactory.getLogger(WarrantyDetailsResource.class);

    private static final String ENTITY_NAME = "warrantyDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final com.laptop.shopping.repository.iRepository.IWarrantyDetailsRepository IWarrantyDetailsRepository;

    private final IWarrantyDetailsService IWarrantyDetailsService;

    public WarrantyDetailsResource(IWarrantyDetailsRepository IWarrantyDetailsRepository, IWarrantyDetailsService IWarrantyDetailsService) {
        this.IWarrantyDetailsRepository = IWarrantyDetailsRepository;
        this.IWarrantyDetailsService = IWarrantyDetailsService;
    }

    @PostMapping("/warranty-details")
    public ResponseEntity<WarrantyDetails> createWarrantyDetails(@RequestBody WarrantyDetailsDto dto) {
        WarrantyDetails warrantyDetails = new WarrantyDetails();
        warrantyDetails.setWarrantyId(dto.getWarrantyId());
        warrantyDetails.setProductCode(dto.getProductCode());
        warrantyDetails.setNote(dto.getNote());
        warrantyDetails.setReceivedDate(dto.getDateReceived().toInstant());
        warrantyDetails.setPayDate(dto.getDatePay().toInstant());
        WarrantyDetails result = IWarrantyDetailsRepository.save(warrantyDetails);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/warranty-details")
    public List<WarrantyDetails> getAllWarrantyDetails() {
        log.debug("REST request to get all WarrantyDetails");
        return IWarrantyDetailsRepository.findAll();
    }

    @GetMapping("/warranty-details/{id}")
    public ResponseEntity<WarrantyDetails> getWarrantyDetails(@PathVariable Long id) {
        log.debug("REST request to get WarrantyDetails : {}", id);
        Optional<WarrantyDetails> warrantyDetails = IWarrantyDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(warrantyDetails);
    }

    @DeleteMapping("/warranty-details/{id}")
    public ResponseEntity<Void> deleteWarrantyDetails(@PathVariable Long id) {
        log.debug("REST request to delete WarrantyDetails : {}", id);
        IWarrantyDetailsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/warranty-details/findByWarrantyId/{id}")
    public ResponseEntity<List<WarrantyDetailsDto>> findByWarrantyId(@PathVariable Long id){
        List<WarrantyDetailsDto> result = IWarrantyDetailsService.findByWarrantyId(id);
        return ResponseEntity.ok().body(result);
    }
}
