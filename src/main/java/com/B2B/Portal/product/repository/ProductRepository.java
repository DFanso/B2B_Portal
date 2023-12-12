package com.B2B.Portal.product.repository;

import com.B2B.Portal.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // This method retrieves all products associated with a given supplier ID
    List<Product> findBySupplierId(Long supplierId);

    // Add any other custom methods you may need
}
