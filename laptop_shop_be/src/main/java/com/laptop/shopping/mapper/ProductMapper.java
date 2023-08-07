package com.laptop.shopping.mapper;

import com.laptop.shopping.dto.ProductDto;
import com.laptop.shopping.domain.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDto, Product>{
}
