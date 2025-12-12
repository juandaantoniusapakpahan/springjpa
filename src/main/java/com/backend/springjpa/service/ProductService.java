package com.backend.springjpa.service;

import com.backend.springjpa.dto.ProductDto;
import com.backend.springjpa.entity.Product;
import com.backend.springjpa.entity.Seller;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.mapper.ProductMapper;
import com.backend.springjpa.repository.ProductRepository;
import com.backend.springjpa.repository.SellerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private SellerRepository sellerRepository;

    public List<ProductDto> getProductsBySellerId(int page, int size, Long sellerId){
        List<ProductDto> dto = productRepository.findBySellerId(sellerId, PageRequest.of(page, size)).stream().map(
                ProductMapper::toProductDTO).toList();
        if (dto.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        return dto;
    }

    public void createProduct(ProductDto dto) {
        Seller seller = sellerRepository.findById(Long.parseLong(dto.getSellerId())).orElseThrow(
                ()-> new ResourceNotFoundException("Seller not found")
        );

        Product product = ProductMapper.toProduct(dto);
        product.setSeller(seller);
        productRepository.save(product);
    }
}
