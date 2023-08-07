package com.laptop.shopping.repository;

import com.laptop.shopping.repository.iRepository.custom.IOrderItemCustomRepository;
import com.laptop.shopping.dto.OrderItemDto;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class OrderItemCustomRepository implements IOrderItemCustomRepository {

    private EntityManager entityManager;

    public OrderItemCustomRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<OrderItemDto> getListOrderItemByOrderId(Long orderId) {
        StringBuilder sql = new StringBuilder(
            "SELECT oi.id,\n" +
            "o.id as orderId,\n" +
            "p.code as productCode,\n" +
            "p.name as productName,\n" +
            "oi.quantity as quantity,\n" +
            "p.price as productPrice,\n" +
            "oi.price as price\n" +
            "FROM order_item oi LEFT JOIN orders o ON oi.order_id = o.id\n" +
            "LEFT JOIN product p ON oi.product_code = p.code\n" +
            "WHERE oi.order_id = " + orderId
        );
        NativeQuery<OrderItemDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("orderId", new LongType())
            .addScalar("productCode", new StringType())
            .addScalar("productName", new StringType())
            .addScalar("productPrice", new FloatType())
            .addScalar("quantity", new IntegerType())
            .addScalar("price", new FloatType())
            .setResultTransformer(Transformers.aliasToBean(OrderItemDto.class));
        return query.list();
    }

    @Override
    public List<OrderItemDto> getAllOrder() {
        StringBuilder sql = new StringBuilder(
            "SELECT order_item.id,order_item.order_id as orderId,order_item.product_code as productCode,order_item.price,count(order_item.quantity) as amount, SUM(price) as totalPrice\n" +
                "FROM order_item\n" +
                "GROUP BY productCode\n" +
                "ORDER BY amount DESC LIMIT 5"
        );
        NativeQuery<OrderItemDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("orderId", new LongType())
            .addScalar("productCode", new StringType())
            .addScalar("price", new FloatType())
            .addScalar("amount",new IntegerType())
            .addScalar("totalPrice",new IntegerType())
            .setResultTransformer(Transformers.aliasToBean(OrderItemDto.class));
        return query.list();
    }
}
