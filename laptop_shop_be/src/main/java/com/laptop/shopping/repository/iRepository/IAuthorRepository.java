package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorRepository extends JpaRepository<Authority, String> {}
