package com.laptop.shopping.service.iService;

import com.laptop.shopping.dto.WarrantyDetailsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWarrantyDetailsService {
    List<WarrantyDetailsDto> findByWarrantyId(Long id);
}
