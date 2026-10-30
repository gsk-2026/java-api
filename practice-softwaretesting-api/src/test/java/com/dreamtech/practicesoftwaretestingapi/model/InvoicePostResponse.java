package com.dreamtech.practicesoftwaretestingapi.model;

import lombok.Builder;

@Builder
public record InvoicePostResponse (

        Integer user_id,
        String billing_address,
        String billing_city,
        String billing_state,
        String billing_postcode,
        String billing_country,
        String payment_method,
        String payment_account_name,
        String payment_account_number,
        double total,
        String invoice_date,
        String invoice_number,
        Integer id

){}
