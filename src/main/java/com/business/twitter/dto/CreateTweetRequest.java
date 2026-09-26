package com.business.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTweetRequest (
        @NotNull
        Long userId,
        @NotBlank
        @Size(max = 280)
        String content
){};
