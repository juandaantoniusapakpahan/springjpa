package com.backend.springjpa.repository;

import com.backend.springjpa.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    boolean existsByEmail(String email);
}
