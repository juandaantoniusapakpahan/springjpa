package com.backend.springjpa.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter @Getter
public class ReviewDto {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Product Variant Id is required")
    private String productVariantId;

    @NotBlank(message = "Rating is required")
    @Pattern(regexp = "^[1-5]$", message = "Rating must be between 1 and 5")
    private String rating;

    private String comment;
}
