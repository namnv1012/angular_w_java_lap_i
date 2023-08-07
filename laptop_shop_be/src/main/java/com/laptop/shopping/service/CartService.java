package com.laptop.shopping.service;

import com.laptop.shopping.domain.User;
import com.laptop.shopping.dto.CartDto;
import com.laptop.shopping.repository.iRepository.custom.ICartCustomRepository;
import com.laptop.shopping.service.iService.ICartService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CartService implements ICartService {

    private final UserService userService;

    private final ICartCustomRepository cartRepositoryCustom;

    public CartService(UserService userService, ICartCustomRepository cartRepositoryCustom) {
        this.userService = userService;
        this.cartRepositoryCustom = cartRepositoryCustom;
    }

    @Override
    public List<CartDto> getListByUerLogin() {
        Optional<User> userLogin = userService.getUserWithAuthorities();
        return cartRepositoryCustom.getListByUserLogin(userLogin.get().getLogin());
    }
}
