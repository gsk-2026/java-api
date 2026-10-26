package com.dreamtech.toolshopapi.model;

import lombok.Builder;

@Builder
public record UserRefreshResponse (

        String access_token,
        String token_type,
        String expires_in

){}
