package com.business.twitter.dto;

public record MediaUploadUrlResponse(
        Long id,
        String mediaKey,
        String mediaUrl
) {}
