package com.dreamtech.toolshopapi.model;

import lombok.Builder;

@Builder
public record UserUpdateRequest (

        String first_name,
        String last_name,
        String address,
        String city,
        String state,
        String country,
        String postcode,
        String phone,
        String dob,
        String email,
        String password

) {}