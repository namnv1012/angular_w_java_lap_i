package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.Warranty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IWarrantyRepository extends JpaRepository<Warranty, Long> {}
