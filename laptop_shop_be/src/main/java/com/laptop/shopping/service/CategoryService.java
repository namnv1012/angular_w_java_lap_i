package com.laptop.shopping.service;

import com.laptop.shopping.dto.CategoryDto;
import com.laptop.shopping.dto.SearchCategory;
import com.laptop.shopping.repository.iRepository.custom.ICategoryCustomRepository;
import com.laptop.shopping.service.iService.ICategoryService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CategoryService implements ICategoryService {

    private final com.laptop.shopping.repository.iRepository.custom.ICategoryCustomRepository ICategoryCustomRepository;

    public CategoryService(ICategoryCustomRepository ICategoryCustomRepository) {
        this.ICategoryCustomRepository = ICategoryCustomRepository;
    }

    @Override
    public Map<String, Object> search(SearchCategory searchCategory, Integer page, Integer pageSize) {
        List<CategoryDto> lstCategory = ICategoryCustomRepository.search(searchCategory, page, pageSize);
        Integer total = ICategoryCustomRepository.getListCategory(searchCategory).size();
        Map<String, Object> result = new HashMap<>();
        result.put("lstCategory", lstCategory);
        result.put("total", total);
        return result;
    }
}
