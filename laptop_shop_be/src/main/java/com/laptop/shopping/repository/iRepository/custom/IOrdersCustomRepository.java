package com.laptop.shopping.repository.iRepository.custom;

import com.laptop.shopping.domain.Orders;
import com.laptop.shopping.dto.SearchCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrdersCustomRepository {
    List<Orders> search(SearchCategory searchCategory, Integer page, Integer pageSize);
    List<Orders> totalOrder(SearchCategory searchCategory);
}
