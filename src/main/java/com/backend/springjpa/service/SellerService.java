package com.backend.springjpa.service;

import com.backend.springjpa.dto.SellerDto;
import com.backend.springjpa.entity.Seller;
import com.backend.springjpa.exception.ConflictException;
import com.backend.springjpa.mapper.SellerMapper;
import com.backend.springjpa.repository.SellerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
    private SellerRepository sellerRepository;

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
}
