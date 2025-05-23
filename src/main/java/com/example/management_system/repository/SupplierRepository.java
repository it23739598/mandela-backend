package com.example.management_system.repository;

import com.example.management_system.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query("SELECT s FROM Supplier s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(s.phone) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(s.contactPerson.firstName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(s.contactPerson.lastName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(s.contactPerson.phone) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(s.address.street) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(s.address.city) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(s.address.state) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(s.address.country) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Supplier> searchSuppliers(@Param("term") String term);
}
