package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.Cart;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
}
