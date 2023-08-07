package com.laptop.shopping.mapper;

import com.laptop.shopping.dto.ProductDto;
import com.laptop.shopping.domain.Product;

public class ProductCustomMapper {
    private ProductCustomMapper() {
    }

    public static ProductDto toDto(Product entity) {
        if (entity == null) {
            return null;
        }
        ProductDto productDTO = new ProductDto();
        productDTO.setId(entity.getId());
        productDTO.setCode(entity.getCode());
        productDTO.setCategoryCode(entity.getCategoryCode());
        productDTO.setProducerCode(entity.getProducerCode());
        productDTO.setName(entity.getName());
        productDTO.setImageUrl(entity.getImageUrl());
        productDTO.setQuantity(entity.getQuantity());
        productDTO.setPrice(entity.getPrice());
        productDTO.setStatus(entity.getStatus());
        productDTO.setCreateDate(entity.getCreateDate());
        productDTO.setCreateName(entity.getCreateName());
        productDTO.setUpdateDate(entity.getUpdateDate());
        productDTO.setUpdateName(entity.getUpdateName());
        productDTO.setContent(entity.getContent());
        productDTO.setSynopsis(entity.getSynopsis());
        productDTO.setScreenSize(entity.getScreenSize());
        productDTO.setResolution(entity.getResolution());
        productDTO.setOs(entity.getOs());
        productDTO.setCpu(entity.getCpu());
        productDTO.setGpu(entity.getGpu());
        productDTO.setRam(entity.getRam());
        productDTO.setRom(entity.getRom());
        productDTO.setBatteryCapacity(entity.getBatteryCapacity());
        productDTO.setWeight(entity.getWeight());
        productDTO.setNetwordConnect(entity.getNetwordConnect());
        productDTO.setSale(entity.getSale());
        productDTO.setImageUrl2(entity.getImageUrl2());
        productDTO.setImageUrl3(entity.getImageUrl3());
        productDTO.setGuarantee(entity.getGuarantee());

        return productDTO;
    }


    public static Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setContent(dto.getContent());
        product.setId(dto.getId());
        product.setCode(dto.getCode());
        product.setCategoryCode(dto.getCategoryCode());
        product.setProducerCode(dto.getProducerCode());
        product.setName(dto.getName());
        product.setImageUrl(dto.getImageUrl());
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());
        product.setStatus(dto.getStatus());
        product.setCreateDate(dto.getCreateDate());
        product.setCreateName(dto.getCreateName());
        product.setUpdateDate(dto.getUpdateDate());
        product.setUpdateName(dto.getUpdateName());
        product.setSynopsis(dto.getSynopsis());
        product.setScreenSize(dto.getScreenSize());
        product.setResolution(dto.getResolution());
        product.setOs(dto.getOs());
        product.setCpu(dto.getCpu());
        product.setGpu(dto.getGpu());
        product.setRam(dto.getRam());
        product.setRom(dto.getRom());
        product.setBatteryCapacity(dto.getBatteryCapacity());
        product.setWeight(dto.getWeight());
        product.setNetwordConnect(dto.getNetwordConnect());
        product.setSale(dto.getSale());
        return product;
    }
}
