package com.laptop.shopping.service.iService;

import com.laptop.shopping.dto.SearchCategory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ICategoryService {
    Map<String, Object> search(SearchCategory searchCategory, Integer page, Integer pageSize);
}
