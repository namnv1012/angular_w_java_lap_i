package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.Producer;
import liquibase.pro.packaged.Q;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProducerRepository extends JpaRepository<Producer, Long> {

    @Query(value = "SELECT * FROM producer p WHERE p.code = ?1", nativeQuery = true)
    Producer findByCode(String code);


    @Query(value = "SELECT * FROM producer p WHERE p.code = ?1", nativeQuery = true)
    Producer findByCodeAndId(String code, Integer id);


    @Query(value = "SELECT * FROM producer p WHERE p.status = 1", nativeQuery = true)
    List<Producer> getAllByStatusActive();
}
