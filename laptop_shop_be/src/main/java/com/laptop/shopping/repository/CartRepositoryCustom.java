package com.laptop.shopping.repository;

import com.laptop.shopping.repository.iRepository.custom.ICartCustomRepository;
import com.laptop.shopping.dto.CartDto;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class CartRepositoryCustom implements ICartCustomRepository {

    private  EntityManager entityManager;

    public CartRepositoryCustom(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<CartDto> getListByUserLogin(String userCode) {
        StringBuilder sql = new StringBuilder(
            "select c.id,\n" +
                "p.code as productCode,\n" +
                "p.name as productName,\n" +
                "p.content as productContent,\n" +
                "p.price as price,\n" +
                "c.quantity\n" +
                " from cart c inner join product p " +
                "on c.product_code = p.code " +
                "where c.user_code = '" + userCode + "'"
        );
        NativeQuery<CartDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("productCode", new StringType())
            .addScalar("productName", new StringType())
            .addScalar("price", new FloatType())
            .addScalar("quantity", new IntegerType())
            .addScalar("price", new FloatType())
            .addScalar("productContent", new StringType())
            .setResultTransformer(Transformers.aliasToBean(CartDto.class));
        return query.list();
    }
}
