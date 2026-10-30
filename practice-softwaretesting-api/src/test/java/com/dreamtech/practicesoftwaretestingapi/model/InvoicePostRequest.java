package com.dreamtech.practicesoftwaretestingapi.model;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record InvoicePostRequest(

        @NotBlank(message = "Invoice user_id cannot be blank")
        Integer user_id,

        @NotBlank(message = "Invoice billing_address cannot be blank")
        String billing_address,

        @NotBlank(message = "Invoice billing_city cannot be blank")
        String billing_city,

        @NotBlank(message = "Invoice billing_state cannot be blank")
        String billing_state,

        @NotBlank(message = "Invoice billing_postcode cannot be blank")
        String billing_postcode,

        @NotBlank(message = "Invoice billing_country cannot be blank")
        String billing_country,

        @NotBlank(message = "Invoice payment_method cannot be blank")
        String payment_method,

        @NotBlank(message = "Invoice payment_account_name cannot be blank")
        String payment_account_name,

        @NotBlank(message = "Invoice payment_account_number cannot be blank")
        String payment_account_number,

        @NotBlank(message = "Invoice total cannot be blank")
        double total,

        @NotBlank(message = "Invoice list cannot be blank")
        List<InvoiceModel> invoicelines

) {
}
