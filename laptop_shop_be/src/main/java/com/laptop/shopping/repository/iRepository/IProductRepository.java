package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM product p WHERE p.id = ?1", nativeQuery = true)
    Product getProductById(Long id);

    @Query(value = "SELECT * FROM product p where p.name like %?1%", nativeQuery = true)
    List<Product> search(String name);

    @Query(value = "SELECT * FROM product p WHERE p.category_code = ?1", nativeQuery = true)
    List<Product> getListProductByCategoryId(String categoryCode);

    // Lấy thông tin danh sách 16 sản phẩm mới nhất
    @Query(value = "SELECT * FROM product p where p.quantity > 0 order by p.price desc limit 0, 16", nativeQuery = true)
    List<Product> getListProductOrderCreateDate();

    @Query(value = "SELECT * FROM product p where p.sale != 0 and p.quantity > 0 order by p.price desc limit 0, 16", nativeQuery = true)
    List<Product> getListProductOrderSale();

    @Query(value = "SELECT * FROM product p ORDER BY p.price DESC LIMIT 0, 12", nativeQuery = true)
    List<Product> getListProductCustomer();

    @Query(value = "SELECT * FROM product p WHERE p.producer_code = ?1 LIMIT 0, 8", nativeQuery = true)
    List<Product> getListProductByProducerCode(String code);

    @Query(value = "SELECT * FROM product p WHERE p.code =?1", nativeQuery = true)
    Product findByCode(String code);

    @Query(value = "SELECT * FROM product p WHERE p.producer_code = ?1 AND p.status = 1 LIMIT 0,12", nativeQuery = true)
    List<Product> findByProducerCode(String code);

    @Query(value = "SELECT p.*\n" +
        "FROM warranty w join orders o on w.order_id = o.id\n" +
        "join order_item oi on o.id = oi.order_id\n" +
        "join product p on oi.product_code = p.code\n" +
        "WHERE w.id = ?1", nativeQuery = true)
    List<Product> findByWarrantyId(Long id);
}
