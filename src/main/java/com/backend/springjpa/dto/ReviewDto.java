package com.backend.springjpa.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDto {

    private String userId;

    @NotBlank(message = "Review id is required")
    private String reviewId;

    @NotBlank(message = "Order item id is required")
    private String orderItemId;

    private String productVariantId;

    @NotBlank(message = "Rating is required")
    @Pattern(regexp = "^[1-5]$", message = "Rating must be between 1 and 5")
    private String rating;

    private String comment;
}
