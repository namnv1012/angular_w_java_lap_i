package com.laptop.shopping.repository.iRepository.custom;

import com.laptop.shopping.dto.ProductDto;
import com.laptop.shopping.dto.SearchProducer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductCustomRepository {
    List<ProductDto> search(SearchProducer searchProducer, Integer page, Integer pageSize);
    List<ProductDto> getTotal(SearchProducer searchProducer);
    List<ProductDto> getAllProductIventory();
}
