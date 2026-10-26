package com.dreamtech.toolshopapi.model;

import lombok.Builder;
import java.util.List;

@Builder
public record InvoiceGetResponse (

        int id,
        String user_id,
        String invoice_date,
        String invoice_number,
        String billing_address,
        String billing_city,
        String billing_state,
        String billing_country,
        String billing_postcode,
        double total,
        String payment_method,
        String payment_account_name,
        String payment_account_number,
        String status,
        String status_message,
        List<Object> invoicelines

) {}
