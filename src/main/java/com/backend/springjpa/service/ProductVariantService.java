package com.backend.springjpa.service;

import com.backend.springjpa.dto.ProductVariantDto;
import com.backend.springjpa.entity.Product;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.exception.ConflictException;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.mapper.ProductVariantMapper;
import com.backend.springjpa.repository.ProductRepository;
import com.backend.springjpa.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

@Service
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(ProductVariantRepository productVariantRepository, ProductRepository productRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }

    public void createProductVariant(ProductVariantDto dto) {
        Product product = productRepository.findById(Long.parseLong(dto.getProductId())).orElseThrow(() ->
                new ResourceNotFoundException("Product not found"));
        ProductVariant variant = ProductVariantMapper.toProductVariant(dto);
        variant.setProduct(product);
        try {
            productVariantRepository.save(variant);
        }catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Product with sku " + dto.getSku() + " already exists");
        }
    }

    @Transactional
    public int updatePriceByPercentage(ProductVariantDto dto) {
        try {
            BigDecimal pct = new BigDecimal(dto.getPercentage()).divide(BigDecimal.valueOf(100),4, RoundingMode.HALF_UP);
            return productVariantRepository.updatePriceByPercentage(Long.parseLong(dto.getProductId()),pct);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
