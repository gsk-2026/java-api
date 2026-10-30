package com.dreamtech.practicesoftwaretestingapi.model;

import lombok.Builder;

@Builder
public record UserGetResponse (

    int id,
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
    String created_at

){}
