package com.dreamtech.toolshopapi.model;

import lombok.Builder;

@Builder
public record UserLoginResponse (

        String access_token,
        String token_type,
        Integer expires_in

){}
