package com.dreamtech.toolshopapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record InvoiceStatusUpdateRequest (

        @NotBlank(message = "Invoice status cannot be blank")
        String status,

        @NotBlank(message = "Invoice status_message cannot be blank")
        String status_message

) {}