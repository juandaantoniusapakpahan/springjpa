package com.backend.springjpa.repository;

import com.backend.springjpa.entity.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySellerId(Long sellerId, PageRequest pageRequest);
    List<Product> findByCategoryContaining(String category);
    List<Product> findByDeletedFalse(Pageable pageable);
    List<Product> findByDeletedFalseAndSellerId(Long sellerId, Pageable pageable);
    List<Product> findByDeletedFalseAndCategoryContaining(String category);
    
}
