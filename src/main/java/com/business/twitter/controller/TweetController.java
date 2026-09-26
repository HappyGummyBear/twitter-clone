package com.business.twitter.controller;

import com.business.twitter.dto.*;
import com.business.twitter.service.TweetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tweet")
public class TweetController {
    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public ResponseEntity<TweetResponse> createTweet(@Valid @RequestBody CreateTweetRequest createRequest) {

        TweetResponse response = tweetService.createTweet(createRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{tweetId}")
    public ResponseEntity<TweetResponse> getTweet(@PathVariable Long tweetId) {
        TweetResponse response = tweetService.getTweet(tweetId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AllTweetsResponse> getUserTweets(@PathVariable Long userId) {
        AllTweetsResponse response = tweetService.getAllTweets(userId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long tweetId) {
        tweetService.deleteTweet(tweetId);

        return ResponseEntity.ok().build();
    }
}
