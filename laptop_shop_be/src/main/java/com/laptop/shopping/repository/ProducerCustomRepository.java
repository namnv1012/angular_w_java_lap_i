package com.laptop.shopping.repository;

import com.laptop.shopping.repository.iRepository.custom.IProducerCustomRepository;
import com.laptop.shopping.dto.ProducerDto;
import com.laptop.shopping.dto.SearchProducer;
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
public class ProducerCustomRepository implements IProducerCustomRepository {

    private EntityManager entityManager;

    public ProducerCustomRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<ProducerDto> search(SearchProducer searchProducer, Integer page, Integer pageSize) {
        StringBuilder sql = new StringBuilder(
            "SELECT" +
                " p.id, " +
                " p.code," +
                " p.name," +
                " p.status," +
                " case when p.status = 0 then 'Đang khóa' else 'Đang hoạt động' end as statusStr, " +
                " p.create_date as createDate, p.create_name as createName, " +
                " p.update_date as updateDate, p.update_name as updateName " +
                " FROM producer p " +
                " WHERE 1 = 1 "
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
        NativeQuery<ProducerDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
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
            .setResultTransformer(Transformers.aliasToBean(ProducerDto.class));
        if (null != searchProducer.getName()) {
            query.setParameter("name", searchProducer.getName());
        }
        if (null != searchProducer.getStatus()) {
            query.setParameter("status", searchProducer.getStatus());
        }
        return query.list();
    }

    @Override
    public List<ProducerDto> getListBySeaProducer(SearchProducer searchProducer) {
        StringBuilder sql = new StringBuilder(
            "SELECT" +
                " p.id, " +
                " p.code," +
                " p.name," +
                " p.status," +
                " case when p.status = 0 then 'Đang khóa' else 'Đang hoạt động' end as statusStr, " +
                " p.create_date as createDate, p.create_name as createName, " +
                " p.update_date as updateDate, p.update_name as updateName " +
                " FROM producer p " +
                " WHERE 1 = 1 "
        );
        if (null != searchProducer.getName()) {
            sql.append(" AND (UPPER(p.code) like CONCAT('%', UPPER(:name), '%') or UPPER(p.name) like CONCAT('%', UPPER(:name), '%') )");
        }
        if (null != searchProducer.getStatus()) {
            sql.append(" AND p.status = :status ");
        }
        NativeQuery<ProducerDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
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
            .setResultTransformer(Transformers.aliasToBean(ProducerDto.class));
        if (null != searchProducer.getName()) {
            query.setParameter("name", searchProducer.getName());
        }
        if (null != searchProducer.getStatus()) {
            query.setParameter("status", searchProducer.getStatus());
        }
        return query.list();
    }
}
