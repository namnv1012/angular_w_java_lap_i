package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.Category;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    //    namnv08: tìm kiếm sản phẩm trong giỏ hàng theo mã code
    @Query(value = "SELECT * FROM category c WHERE c.code = ?1", nativeQuery = true)
    Category findByCode(String code);

    @Query(value = "SELECT * FROM category p WHERE p.code = ?1", nativeQuery = true)
    Category findByCodeAndId(String code, Integer id);

    //    Lấy ra tất cả sản phẩm trong giỏ hàng đang hoạt động
    @Query(value = "SELECT * FROM category c WHERE c.status = 1", nativeQuery = true)
    List<Category> getAllStatusActive();
}
