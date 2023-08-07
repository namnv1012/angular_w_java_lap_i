package com.laptop.shopping.repository;

import com.laptop.shopping.domain.Orders;
import com.laptop.shopping.repository.iRepository.custom.IOrdersCustomRepository;
import com.laptop.shopping.dto.SearchCategory;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class OrdersCustomRepository implements IOrdersCustomRepository {

    private EntityManager entityManager;

    public OrdersCustomRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<Orders> search(SearchCategory searchCategory, Integer page, Integer pageSize) {
        StringBuilder sql = new StringBuilder(
            "SELECT o.id,\n" +
                "o.user_code as userCode,\n" +
                "o.name,\n" +
                "o.phone,\n" +
                "o.email,\n" +
                "o.address,\n" +
                "o.note,\n" +
                "o.status,\n" +
                "o.order_date as orderDate\n" +
                "FROM orders o\n" +
                " WHERE 1 = 1 \n"
        );
        if (null != searchCategory.getName()) {
            sql.append(" AND (UPPER(o.user_code) like CONCAT('%', UPPER(:name), '%') or UPPER(o.name) like CONCAT('%', UPPER(:name), '%') )");
        }
        if (null != searchCategory.getStatus()) {
            sql.append(" AND o.status = :status ");
        }
        if (page != null && pageSize != null) {
            Integer offset;
            if (page <= 1) {
                offset = 0;
            } else {
                offset = (page - 1) * pageSize;
            }
            sql.append("  LIMIT " + offset + " , " + pageSize + " ");
        }
        NativeQuery<Orders> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("userCode", new StringType())
            .addScalar("name", new StringType())
            .addScalar("phone", new StringType())
            .addScalar("email", new StringType())
            .addScalar("address", new StringType())
            .addScalar("note", new StringType())
            .addScalar("status", new IntegerType())
            .addScalar("orderDate", new InstantType())
            .setResultTransformer(Transformers.aliasToBean(Orders.class));
        if (null != searchCategory.getName()) {
            query.setParameter("name", searchCategory.getName());
        }
        if (null != searchCategory.getStatus()) {
            query.setParameter("status", searchCategory.getStatus());
        }
        return query.list();
    }

    @Override
    public List<Orders> totalOrder(SearchCategory searchCategory) {
        StringBuilder sql = new StringBuilder(
            "SELECT o.id,\n" +
                "o.user_code as userCode,\n" +
                "o.name,\n" +
                "o.phone,\n" +
                "o.email,\n" +
                "o.address,\n" +
                "o.note,\n" +
                "o.status,\n" +
                "o.order_date as orderDate\n" +
                "FROM orders o\n" +
                " WHERE 1 = 1 \n"
        );
        if (null != searchCategory.getName()) {
            sql.append(" AND (UPPER(o.user_code) like CONCAT('%', UPPER(:name), '%') or UPPER(o.name) like CONCAT('%', UPPER(:name), '%') )");
        }
        if (null != searchCategory.getStatus()) {
            sql.append(" AND o.status = :status ");
        }
        NativeQuery<Orders> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("userCode", new StringType())
            .addScalar("name", new StringType())
            .addScalar("phone", new StringType())
            .addScalar("email", new StringType())
            .addScalar("address", new StringType())
            .addScalar("note", new StringType())
            .addScalar("status", new IntegerType())
            .addScalar("orderDate", new InstantType())
            .setResultTransformer(Transformers.aliasToBean(Orders.class));
        if (null != searchCategory.getName()) {
            query.setParameter("name", searchCategory.getName());
        }
        if (null != searchCategory.getStatus()) {
            query.setParameter("status", searchCategory.getStatus());
        }
        return query.list();
    }
}
