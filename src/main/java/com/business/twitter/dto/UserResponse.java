package com.business.twitter.dto;

public record UserResponse(
        String userName,
        String firstName,
        String lastName,
        String email
) {};
