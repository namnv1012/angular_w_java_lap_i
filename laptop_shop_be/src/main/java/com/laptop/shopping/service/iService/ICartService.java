package com.laptop.shopping.service.iService;

import com.laptop.shopping.dto.CartDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICartService {
    List<CartDto> getListByUerLogin();
}
