package com.dreamtech.practicesoftwaretestingapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record InvoiceModel (

        @NotBlank(message = "Invoice product_id cannot be blank")
        long product_id,

        @NotBlank(message = "Invoice unit_price cannot be blank")
        double unit_price,

        @NotBlank(message = "Invoice quantity cannot be blank")
        int quantity
){}
