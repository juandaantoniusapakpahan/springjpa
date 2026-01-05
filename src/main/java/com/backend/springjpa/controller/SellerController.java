package com.backend.springjpa.controller;

import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.SellerBestSellingProductReport;
import com.backend.springjpa.dto.SellerDto;
import com.backend.springjpa.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {
    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }
    @PostMapping("/addSeller")
    public ResponseEntity<ApiResponse<SellerDto>> createSeller(@Valid @RequestBody SellerDto sellerDto) {
        sellerService.createSeller(sellerDto);
        return ResponseEntity.ok(ApiResponse.ok("Seller berhasil ditambahkan",sellerDto, "/addSeller"));
    }

    @PostMapping("/getAllSeller")
    public ResponseEntity<ApiResponse<List<SellerDto>>> getSellers(@RequestParam int page, @RequestParam int size) {
        List<SellerDto> sellerDtos = sellerService.getSellers(page, size);
        return ResponseEntity.ok(ApiResponse.ok("Success",sellerDtos, "/getAllSeller"));
    }

    @PostMapping("/softDeleteSeller/{sellerId}")
    public ResponseEntity<ApiResponse<String>> softDeleteSeller(@PathVariable Long sellerId) {
        sellerService.softDeleteSeller(sellerId);
        return ResponseEntity.ok(ApiResponse.ok("Seller deleted successfully",null, "/softDeleteSeller"));
    }

    @PostMapping("/activateSeller/{sellerId}")
    public ResponseEntity<ApiResponse<String>> activateSeller(@PathVariable Long sellerId) {
        sellerService.activateSeller(sellerId);
        return ResponseEntity.ok(ApiResponse.ok("Seller activated successfully",null, "/activateSeller"));
    }

    @PostMapping("/best-selling-product")
    public ResponseEntity<ApiResponse<SellerBestSellingProductReport>> getSellerBestSellingProduct(@RequestParam Long sellerId,
                                                                                                   @RequestParam LocalDate start,
                                                                                                   @RequestParam LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok("Success", sellerService.getSellerBestSellingProduct(sellerId, start,end), "/best-selling-product" ));
    }

}
