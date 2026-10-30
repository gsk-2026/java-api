package com.dreamtech.practicesoftwaretestingapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public record UserLoginRequest (

        @NotBlank(message = "User email cannot be blank")
        String email,

        @NotBlank(message = "User password cannot be blank")
        String password

) {}
