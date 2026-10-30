package com.dreamtech.practicesoftwaretestingapi.model;

import lombok.Builder;


@Builder
public record UserLoginResponse (

        String access_token,
        String token_type,
        Integer expires_in

){}
