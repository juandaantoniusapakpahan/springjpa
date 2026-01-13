package com.backend.springjpa.service;

import com.backend.springjpa.dto.ProductVariantAverage;
import com.backend.springjpa.dto.ProductVariantDto;
import com.backend.springjpa.dto.ReviewDto;
import com.backend.springjpa.dto.StockRiskReport;
import com.backend.springjpa.entity.Product;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.exception.BadRequestException;
import com.backend.springjpa.exception.ConflictException;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.mapper.ProductVariantMapper;
import com.backend.springjpa.mapper.ReviewMapper;
import com.backend.springjpa.repository.ProductRepository;
import com.backend.springjpa.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(ProductVariantRepository productVariantRepository,
                                 ProductRepository productRepository) {
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
            int count = productVariantRepository.updatePriceByPercentage(Long.parseLong(dto.getProductId()),pct);
            if (count < 1) {
                throw new RuntimeException("not");
            }
            return count;
        } catch (Exception e) {
            if (e.getMessage().equals("not")){
                throw new ResourceNotFoundException("Product variant not found");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ProductVariantDto> getAllProductVariant(Pageable pageable) {
        return productVariantRepository.findAll(pageable)
                .stream()
                .map(ProductVariantMapper::toProductVariantDto)
                .toList();
    }

    public List<StockRiskReport> getStockRisk(int highRisk, int mediumRisk) {
        return productVariantRepository.getStockRiskReport(highRisk, mediumRisk);
    }

    public ProductVariant getProductVariantById(Long id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));
    }

    public ProductVariantDto getProductVariantAndReview(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product Variant not found"));

        ProductVariantDto dto = ProductVariantMapper.toProductVariantDto(productVariant);
        List<ReviewDto> reviewDto = productVariant.getReviews().stream().map(ReviewMapper::entityToDto).toList();
        dto.setReviews(reviewDto);
        return dto;
    }

    public ProductVariantAverage getProductVariantAvgRate(Long variantId, LocalDate start, LocalDate end) {
        if (variantId == null || start == null || end == null) {
            throw new BadRequestException("Request param is required");
        }
        return productVariantRepository.getProductVariantAverage(variantId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }
}
