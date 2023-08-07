package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.WarrantyDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IWarrantyDetailsRepository extends JpaRepository<WarrantyDetails, Long> {}
