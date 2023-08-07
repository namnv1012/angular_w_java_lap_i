package com.laptop.shopping.repository;

import com.laptop.shopping.repository.iRepository.custom.ICategoryCustomRepository;
import com.laptop.shopping.dto.CategoryDto;
import com.laptop.shopping.dto.SearchCategory;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.InstantType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class CategoryCustomRepository implements ICategoryCustomRepository {

    private EntityManager entityManager;

    public CategoryCustomRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public List<CategoryDto> search(SearchCategory searchCategory, Integer page, Integer pageSize) {
        StringBuilder sql = new StringBuilder(
            "SELECT" +
                " c.id, " +
                " c.code," +
                " c.name," +
                " c.status," +
                " case when c.status = 0 then 'Đang khóa' else 'Đang hoạt động' end as statusStr, " +
                " c.create_date as createDate, c.create_name as createName, " +
                " c.update_date as updateDate, c.update_name as updateName " +
                " FROM category c " +
                " WHERE 1 = 1 "
        );
        if (null != searchCategory.getName()) {
            sql.append(" AND (UPPER(c.code) like CONCAT('%', UPPER(:name), '%') or UPPER(c.name) like CONCAT('%', UPPER(:name), '%') )");
        }
        if (null != searchCategory.getStatus()) {
            sql.append(" AND c.status = :status ");
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
        NativeQuery<CategoryDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("code", new StringType())
            .addScalar("name", new StringType())
            .addScalar("status", new BooleanType())
            .addScalar("statusStr", new StringType())
            .addScalar("createDate", new InstantType())
            .addScalar("createName", new StringType())
            .addScalar("updateDate", new InstantType())
            .addScalar("updateName", new StringType())
            .setResultTransformer(Transformers.aliasToBean(CategoryDto.class));
        if (null != searchCategory.getName()) {
            query.setParameter("name", searchCategory.getName());
        }
        if (null != searchCategory.getStatus()) {
            query.setParameter("status", searchCategory.getStatus());
        }
        return query.list();
    }

    @Override
    public List<CategoryDto> getListCategory(SearchCategory searchCategory) {
        StringBuilder sql = new StringBuilder(
            "SELECT" +
                " c.id, " +
                " c.code," +
                " c.name," +
                " c.status," +
                " case when c.status = 0 then 'Đang khóa' else 'Đang hoạt động' end as statusStr, " +
                " c.create_date as createDate, c.create_name as createName, " +
                " c.update_date as updateDate, c.update_name as updateName " +
                " FROM category c " +
                " WHERE 1 = 1 "
        );
        if (null != searchCategory.getName()) {
            sql.append(" AND (UPPER(c.code) like CONCAT('%', UPPER(:name), '%') or UPPER(c.name) like CONCAT('%', UPPER(:name), '%') )");
        }
        if (null != searchCategory.getStatus()) {
            sql.append(" AND c.status = :status");
        }
        NativeQuery<CategoryDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("code", new StringType())
            .addScalar("name", new StringType())
            .addScalar("status", new BooleanType())
            .addScalar("statusStr", new StringType())
            .addScalar("createDate", new InstantType())
            .addScalar("createName", new StringType())
            .addScalar("updateDate", new InstantType())
            .addScalar("updateName", new StringType())
            .setResultTransformer(Transformers.aliasToBean(CategoryDto.class));
        if (null != searchCategory.getName()) {
            query.setParameter("name", searchCategory.getName());
        }
        if (null != searchCategory.getStatus()) {
            query.setParameter("status", searchCategory.getStatus());
        }
        return query.list();
    }
}
