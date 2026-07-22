package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findProductByProductname(String productname);

    Product findProductByProductCode(String productCode);
}