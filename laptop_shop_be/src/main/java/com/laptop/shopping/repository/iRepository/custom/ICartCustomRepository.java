package com.laptop.shopping.repository.iRepository.custom;

import com.laptop.shopping.dto.CartDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartCustomRepository {
    List<CartDto> getListByUserLogin(String userCode);
}
