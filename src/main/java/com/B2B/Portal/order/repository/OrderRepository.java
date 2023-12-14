package com.B2B.Portal.order.repository;

import com.B2B.Portal.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o JOIN o.items i WHERE i.supplierId = :supplierId")
    List<Order> findBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.orderId = :orderId")
    Optional<Order> findByIdWithItems(Long orderId);


}
