package com.backend.springjpa.service;

import com.backend.springjpa.dto.SellerBestSellingProductReport;
import com.backend.springjpa.dto.SellerDto;
import com.backend.springjpa.dto.SellerPerformanceReport;
import com.backend.springjpa.entity.Seller;
import com.backend.springjpa.exception.ConflictException;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.mapper.SellerMapper;
import com.backend.springjpa.repository.SellerRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }
    public void createSeller(SellerDto sellerDto) {
        Seller seller = SellerMapper.toSeller(sellerDto);
        try {
            sellerRepository.save(seller);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Email already exists");
        }
    }

    public List<SellerDto> getSellers(int page, int size) {
        Page<Seller> sellers = sellerRepository.findAll(PageRequest.of(page, size));
        return sellers.stream().map(SellerMapper::toSellerDto).toList();
    }

    @Transactional
    public void softDeleteSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        seller.setDeleted(true);
        seller.getProducts().forEach(product -> product.setDeleted(true));
        seller.getProducts().forEach(product -> product.getProductVariants().forEach(productVariant -> productVariant.setDeleted(true)));
        sellerRepository.save(seller);
    }

    @Transactional
    public void activateSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        seller.setDeleted(false);
        seller.getProducts().forEach(product -> product.setDeleted(false));
        seller.getProducts().forEach(product -> product.getProductVariants().forEach(productVariant -> productVariant.setDeleted(false)));
        sellerRepository.save(seller);
    }

    public SellerBestSellingProductReport getSellerBestSellingProduct(Long sellerId, LocalDate start, LocalDate end) {
        return sellerRepository.getSellerBestSellingProduct(sellerId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public List<SellerPerformanceReport> getSellerPerformanceRanking(LocalDate start, LocalDate end) {
        return sellerRepository.getSellerPerformanceRanking(start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }
}
