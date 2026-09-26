package com.business.twitter.dto;

import java.util.List;

public record AllTweetsResponse(
        List<TweetResponse> tweetResponseList
){}
