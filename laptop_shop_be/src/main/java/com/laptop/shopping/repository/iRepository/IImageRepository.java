package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.Image;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface IImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT * FROM image i WHERE i.link_code = ?1", nativeQuery = true)
    List<Image> findByLinkCode(String code);
}
