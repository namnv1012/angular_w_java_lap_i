package com.laptop.shopping.service;

import com.laptop.shopping.dto.WarrantyDetailsDto;
import com.laptop.shopping.repository.iRepository.custom.IWarrantyDetailsCustomRepository;
import com.laptop.shopping.service.iService.IWarrantyDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarrantyDetailsService implements IWarrantyDetailsService {

    private final com.laptop.shopping.repository.iRepository.custom.IWarrantyDetailsCustomRepository IWarrantyDetailsCustomRepository;

    public WarrantyDetailsService(IWarrantyDetailsCustomRepository IWarrantyDetailsCustomRepository) {
        this.IWarrantyDetailsCustomRepository = IWarrantyDetailsCustomRepository;
    }

    @Override
    public List<WarrantyDetailsDto> findByWarrantyId(Long id) {
        return IWarrantyDetailsCustomRepository.findByWarrantyId(id);
    }
}
