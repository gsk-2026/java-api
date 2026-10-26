package com.dreamtech.toolshopapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRegisterRequest(

        @NotBlank(message = "User first_name cannot be blank")
        String first_name,

        @NotBlank(message = "User last_name cannot be blank")
        String last_name,

        @NotBlank(message = "User dob cannot be blank")
        String dob,

        @NotBlank(message = "User address cannot be blank")
        String address,

        @NotBlank(message = "User city cannot be blank")
        String city,

        //@NotBlank(message = "User state cannot be blank")
        String state,

        //@NotBlank(message = "User postcode cannot be blank")
        String postcode,

        @NotBlank(message = "User country cannot be blank")
        String country,

        @NotBlank(message = "User phone cannot be blank")
        String phone,

        @NotBlank(message = "User email cannot be blank")
        String email,

        @NotBlank(message = "User dob cannot be blank")
        String password
) {}