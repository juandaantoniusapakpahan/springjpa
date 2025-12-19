package com.backend.springjpa.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class PaymentDto {

    @NotNull @NotBlank(message = "PaymentId is required")
    private String paymentId;
    @NotNull @NotBlank(message = "TimeStamp is required")
    private String timeStamp;

    @NotNull @NotBlank(message = "Status is required")
    private String paymentStatus;
}
