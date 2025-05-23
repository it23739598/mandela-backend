package com.example.management_system.repository;

import com.example.management_system.model.CustomerOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderItemRepository extends JpaRepository<CustomerOrderItem, Long> {}
