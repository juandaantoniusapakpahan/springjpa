package com.backend.springjpa.service;

import com.backend.springjpa.dto.SellerDto;
import com.backend.springjpa.entity.Seller;
import com.backend.springjpa.exception.ConflictException;
import com.backend.springjpa.mapper.SellerMapper;
import com.backend.springjpa.repository.SellerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerService {
    private SellerRepository sellerRepository;

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
}
