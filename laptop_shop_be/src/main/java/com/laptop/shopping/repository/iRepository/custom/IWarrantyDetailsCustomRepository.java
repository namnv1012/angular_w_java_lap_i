package com.laptop.shopping.repository.iRepository.custom;

import com.laptop.shopping.dto.WarrantyDetailsDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWarrantyDetailsCustomRepository {
    List<WarrantyDetailsDto> findByWarrantyId(Long id);
}
