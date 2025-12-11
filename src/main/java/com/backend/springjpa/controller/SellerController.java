package com.backend.springjpa.controller;

import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.SellerDto;
import com.backend.springjpa.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sellers")
public class SellerController {
    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<SellerDto>> createSeller(@Valid @RequestBody SellerDto sellerDto) {
        sellerService.createSeller(sellerDto);
        return ResponseEntity.ok(ApiResponse.ok("Seller berhasil ditambahkan",sellerDto, "/api/v1/sellers/add"));
    }

}
