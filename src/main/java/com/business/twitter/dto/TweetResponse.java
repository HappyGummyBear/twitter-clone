package com.business.twitter.dto;

import java.time.Instant;
import java.util.List;

public record TweetResponse(
        Long id,
        String userName,
        String content,
        Instant createdAt,
        List<MediaResponse> media
) {};
