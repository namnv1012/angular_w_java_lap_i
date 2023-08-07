package com.laptop.shopping.repository;

import com.laptop.shopping.repository.iRepository.custom.IUserCustomRepository;
import com.laptop.shopping.dto.SearchUser;
import com.laptop.shopping.dto.UserDto;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class UserCustomRepository implements IUserCustomRepository {

    private EntityManager entityManager;

    public UserCustomRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<UserDto> getListUserByRole(String roleName) {
        StringBuilder sql = new StringBuilder(
            "select ju.id,\n" +
                "ju.login as login,\n" +
                "ju.full_name as fullName,\n" +
                "ju.phone_number as phoneNumber,\n" +
                "ju.email as email,\n" +
                "ju.created_date as createDate\n" +
                "from jhi_user ju join jhi_user_authority jua on ju.id  = jua.user_id\n" +
                "join jhi_authority ja on jua.authority_name = ja.name\n" +
                "where jua.authority_name = '" + roleName + "'"
        );
        NativeQuery<UserDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("login", new StringType())
            .addScalar("fullName", new StringType())
            .addScalar("email", new StringType())
            .addScalar("phoneNumber", new StringType())
            .addScalar("createDate", new InstantType())
            .setResultTransformer(Transformers.aliasToBean(UserDto.class));
        return query.list();
    }

    @Override
    public List<UserDto> searchListCustomer(SearchUser searchUser, Integer page, Integer pageSize) {
        StringBuilder sql = new StringBuilder(
            "select ju.id,\n" +
                "ju.login as login,\n" +
                "ju.full_name as fullName,\n" +
                "ju.phone_number as phoneNumber,\n" +
                "ju.email as email,\n" +
                "ju.activated as activated,\n" +
                "ju.created_date as createDate\n" +
                "FROM jhi_user ju join jhi_user_authority jua on ju.id = jua.user_id\n" +
                "join jhi_authority ja on ja.name = jua.authority_name\n" +
                "WHERE ja.name = 'ROLE_CUSTOMER' "
        );
        if (null != searchUser.getName()) {
            sql.append(" AND (UPPER(ju.login) like CONCAT('%', UPPER(:name), '%') )");
        }
        if (null != searchUser.getStatus()) {
            sql.append(" AND ju.activated = :status ");
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
        NativeQuery<UserDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("login", new StringType())
            .addScalar("fullName", new StringType())
            .addScalar("email", new StringType())
            .addScalar("activated", new BooleanType())
            .addScalar("phoneNumber", new StringType())
            .addScalar("createDate", new InstantType())
            .setResultTransformer(Transformers.aliasToBean(UserDto.class));
        if (null != searchUser.getName()) {
            query.setParameter("name", searchUser.getName());
        }
        if (null != searchUser.getStatus()) {
            query.setParameter("status", searchUser.getStatus());
        }
        return query.list();
    }

    @Override
    public List<UserDto> totalListCustomer(SearchUser searchUser) {
        StringBuilder sql = new StringBuilder(
            "select ju.id,\n" +
                "ju.login as login,\n" +
                "ju.full_name as fullName,\n" +
                "ju.phone_number as phoneNumber,\n" +
                "ju.email as email,\n" +
                "ju.activated as activated,\n" +
                "ju.created_date as createDate\n" +
                "FROM jhi_user ju join jhi_user_authority jua on ju.id = jua.user_id\n" +
                "join jhi_authority ja on ja.name = jua.authority_name\n" +
                "WHERE ja.name = 'ROLE_CUSTOMER' "
        );
        if (null != searchUser.getName()) {
            sql.append(" AND (UPPER(ju.login) like CONCAT('%', UPPER(:name), '%') )");
        }
        if (null != searchUser.getStatus()) {
            sql.append(" AND ju.activated = :status ");
        }
        NativeQuery<UserDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("login", new StringType())
            .addScalar("fullName", new StringType())
            .addScalar("email", new StringType())
            .addScalar("activated", new BooleanType())
            .addScalar("phoneNumber", new StringType())
            .addScalar("createDate", new InstantType())
            .setResultTransformer(Transformers.aliasToBean(UserDto.class));
        if (null != searchUser.getName()) {
            query.setParameter("name", searchUser.getName());
        }
        if (null != searchUser.getStatus()) {
            query.setParameter("status", searchUser.getStatus());
        }
        return query.list();
    }
}
