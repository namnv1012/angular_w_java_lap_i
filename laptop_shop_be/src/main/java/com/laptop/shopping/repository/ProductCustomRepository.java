package com.laptop.shopping.repository;

import com.laptop.shopping.repository.iRepository.custom.IProductCustomRepository;
import com.laptop.shopping.dto.ProductDto;
import com.laptop.shopping.dto.SearchProducer;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class ProductCustomRepository implements IProductCustomRepository {

    private EntityManager entityManager;

    public ProductCustomRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<ProductDto> search(SearchProducer searchProducer, Integer page, Integer pageSize) {
        StringBuilder sql = new StringBuilder(
            "SELECT p.id,\n" +
                "p.code,\n" +
                "p.name,\n" +
                "c.name as categoryName,\n" +
                "pr.name as producerName,\n" +
                "p.sale,\n" +
                "p.price,\n" +
                "p.status,\n" +
                "p.quantity,\n" +
                "p.image_url as imageUrl,\n" +
                "p.image_url_2 as imageUrl2,\n" +
                "p.image_url_3 as imageUrl3,\n" +
                "case when p.status = 0 then 'Đang khóa' else 'Đang hoạt động' end as statusStr,\n" +
                "p.create_date as createDate,\n" +
                "p.create_name as createName\n" +
                "FROM product p LEFT JOIN category c ON p.category_code = c.code\n" +
                "LEFT JOIN producer pr on p.producer_code = pr.code\n" +
                " WHERE 1 = 1 \n"
        );
        if (null != searchProducer.getName()) {
            sql.append(" AND (UPPER(p.code) like CONCAT('%', UPPER(:name), '%') or UPPER(p.name) like CONCAT('%', UPPER(:name), '%') )");
        }
        if (null != searchProducer.getStatus()) {
            sql.append(" AND p.status = :status ");
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
        NativeQuery<ProductDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("code", new StringType())
            .addScalar("name", new StringType())
            .addScalar("status", new BooleanType())
            .addScalar("statusStr", new StringType())
            .addScalar("categoryName", new StringType())
            .addScalar("producerName", new StringType())
            .addScalar("imageUrl", new StringType())
            .addScalar("imageUrl2", new StringType())
            .addScalar("imageUrl3", new StringType())
            .addScalar("sale", new FloatType())
            .addScalar("quantity", new IntegerType())
            .addScalar("price", new FloatType())
            .addScalar("createDate", new InstantType())
            .addScalar("createName", new StringType())
            .setResultTransformer(Transformers.aliasToBean(ProductDto.class));
        if (null != searchProducer.getName()) {
            query.setParameter("name", searchProducer.getName());
        }
        if (null != searchProducer.getStatus()) {
            query.setParameter("status", searchProducer.getStatus());
        }
        return query.list();
    }

    @Override
    public List<ProductDto> getTotal(SearchProducer searchProducer) {
        StringBuilder sql = new StringBuilder(
            "SELECT p.id,\n" +
                "p.code,\n" +
                "p.name,\n" +
                "c.name as categoryName,\n" +
                "pr.name as producerName,\n" +
                "p.sale,\n" +
                "p.price,\n" +
                "p.status,\n" +
                "case when p.status = 0 then 'Đang khóa' else 'Đang hoạt động' end as statusStr,\n" +
                "p.create_date as createDate,\n" +
                "p.create_name as createName\n" +
                "FROM product p LEFT JOIN category c ON p.category_code = c.code\n" +
                "LEFT JOIN producer pr on p.producer_code = pr.code\n" +
                " WHERE 1 = 1 \n"
        );
        if (null != searchProducer.getName()) {
            sql.append(" AND (UPPER(p.code) like CONCAT('%', UPPER(:name), '%') or UPPER(p.name) like CONCAT('%', UPPER(:name), '%') )");
        }
        if (null != searchProducer.getStatus()) {
            sql.append(" AND p.status = :status ");
        }
        NativeQuery<ProductDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("code", new StringType())
            .addScalar("name", new StringType())
            .addScalar("status", new BooleanType())
            .addScalar("statusStr", new StringType())
            .addScalar("categoryName", new StringType())
            .addScalar("producerName", new StringType())
            .addScalar("sale", new FloatType())
            .addScalar("price", new FloatType())
            .addScalar("createDate", new InstantType())
            .addScalar("createName", new StringType())
            .setResultTransformer(Transformers.aliasToBean(ProductDto.class));
        if (null != searchProducer.getName()) {
            query.setParameter("name", searchProducer.getName());
        }
        if (null != searchProducer.getStatus()) {
            query.setParameter("status", searchProducer.getStatus());
        }
        return query.list();
    }

    @Override
    public List<ProductDto> getAllProductIventory() {
        StringBuilder sql = new StringBuilder(
            "SELECT p.id,p.code ,p.price,min(p.quantity) as amount\n" +
                "FROM  product p\n" +
                "GROUP BY p.code\n" +
                "ORDER BY amount DESC LIMIT 5"
        );
        NativeQuery<ProductDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("code", new StringType())
            .addScalar("price", new FloatType())
            .addScalar("amount",new IntegerType())
            .setResultTransformer(Transformers.aliasToBean(ProductDto.class));
        return query.list();
    }
}
