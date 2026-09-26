package com.business.twitter.dto;

public record MediaUploadUrlRequest(
        Long tweetId,
        String contentType
){}
