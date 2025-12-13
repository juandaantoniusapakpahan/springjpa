package com.backend.springjpa.service;

import com.backend.springjpa.dto.ProductDto;
import com.backend.springjpa.dto.ProductVariantDto;
import com.backend.springjpa.entity.Product;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.entity.Seller;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.mapper.ProductMapper;
import com.backend.springjpa.mapper.ProductVariantMapper;
import com.backend.springjpa.repository.ProductRepository;
import com.backend.springjpa.repository.SellerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public ProductService(ProductRepository productRepository, SellerRepository sellerRepository) {
        this.productRepository =productRepository;
        this.sellerRepository = sellerRepository;
    }

    public List<ProductDto> getProductsBySellerId(int page, int size, Long sellerId){
        List<ProductDto> dto = productRepository.findBySellerId(sellerId, PageRequest.of(page, size)).stream().map(
                ProductMapper::toProductDTO).toList();
        if (dto.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        return dto;
    }

    public void createProduct(ProductDto dto) {
        Seller seller = sellerRepository.findById(Long.parseLong(dto.getSellerId())).orElseThrow(()->
                new ResourceNotFoundException("Seller not found"));

        Product product = ProductMapper.toProduct(dto);
        product.setSeller(seller);
        productRepository.save(product);
    }


    public List<ProductDto> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategoryContaining(category);
        List<ProductDto> dtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = ProductMapper.toProductDTO(product);
            List<ProductVariantDto> variantsDto = new ArrayList<>();
            for (ProductVariant variant: product.getProductVariants()) {
                ProductVariantDto variantDto = ProductVariantMapper.toProductVariantDto(variant);
                variantsDto.add(variantDto);
            }
            productDto.setVariants(variantsDto);
            dtos.add(productDto);
        }
        return dtos;
    }


}
