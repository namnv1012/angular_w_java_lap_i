package com.laptop.shopping.repository.iRepository.custom;

import com.laptop.shopping.dto.CategoryDto;
import com.laptop.shopping.dto.SearchCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryCustomRepository {
    List<CategoryDto> search(SearchCategory searchCategory, Integer page, Integer pageSize);
    List<CategoryDto> getListCategory(SearchCategory searchCategory);
}
