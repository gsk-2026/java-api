package com.dreamtech.toolshopapi.model;

import lombok.Builder;

@Builder
public record UserRegisterResponse(

        String first_name,
        String last_name,
        String dob,
        String address,
        String city,
        String state,
        String postcode,
        String country,
        String phone,
        String email,
        String created_at,
        Integer id
) {}