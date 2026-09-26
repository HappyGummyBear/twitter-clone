package com.business.twitter.dto;

public record MediaResponse(
        Long id,
        String mediaType,
        String contentType,
        String url
) {}
