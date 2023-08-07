package com.laptop.shopping.repository;

import com.laptop.shopping.repository.iRepository.custom.IWarrantyDetailsCustomRepository;
import com.laptop.shopping.dto.WarrantyDetailsDto;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;
@Component
public class WarrantyDetailsCustomRepository implements IWarrantyDetailsCustomRepository {

    private EntityManager entityManager;

    public WarrantyDetailsCustomRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<WarrantyDetailsDto> findByWarrantyId(Long id) {
        StringBuilder sql = new StringBuilder(
            "SELECT wd.id,\n" +
                "wd.warranty_id as warrantyId,\n" +
                "wd.product_code as productCode,\n" +
                "p.name as productName,\n" +
                "wd.received_date as receivedDate,\n" +
                "wd.pay_date as payDate,\n" +
                "wd.note\n" +
                "FROM warranty_details wd join warranty w on wd.warranty_id = w.id\n" +
                "join product p on wd.product_code = p.code\n" +
                "WHERE wd.warranty_id = " + id
        );
        NativeQuery<WarrantyDetailsDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("warrantyId", new LongType())
            .addScalar("productCode", new StringType())
            .addScalar("productName", new StringType())
            .addScalar("receivedDate", new InstantType())
            .addScalar("payDate", new InstantType())
            .addScalar("note", new StringType())
            .setResultTransformer(Transformers.aliasToBean(WarrantyDetailsDto.class));
        return query.list();
    }
}
