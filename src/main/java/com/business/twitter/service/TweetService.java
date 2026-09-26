package com.business.twitter.service;

import com.business.twitter.dto.AllTweetsResponse;
import com.business.twitter.dto.CreateTweetRequest;
import com.business.twitter.dto.MediaResponse;
import com.business.twitter.dto.TweetResponse;
import com.business.twitter.entity.Tweet;
import com.business.twitter.entity.User;
import com.business.twitter.repository.MediaRepository;
import com.business.twitter.repository.TweetRepository;
import com.business.twitter.repository.UserRepository;
import io.awspring.cloud.s3.S3Template;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final MediaService mediaService;

    TweetService(TweetRepository tweetRepository,
                 UserRepository userRepository, MediaService mediaService) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.mediaService = mediaService;
    }

    public TweetResponse createTweet(CreateTweetRequest createRequest) {
      User user = userRepository.findById(createRequest.userId())
              .orElseThrow(() -> new RuntimeException("User not found."));
      Tweet tweet = new Tweet();

      tweet.setUser(user);
      tweet.setContent(createRequest.content());
      tweet.setCreatedAt(Instant.now());

      Tweet savedTweet = tweetRepository.save(tweet);

      return new TweetResponse(
              savedTweet.getId(),
              savedTweet.getUser().getUserName(),
              savedTweet.getContent(),
              savedTweet.getCreatedAt(),
              null
      );
    };

    public TweetResponse getTweet(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found."));

        return generateResponseWithMedia(tweet);
    }

    public AllTweetsResponse getAllTweets(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found.");
        }

        List<Tweet> allTweets = tweetRepository.findByUserIdOrderByCreatedAtDesc(userId);

        List<TweetResponse> tweetResponses = allTweets
                .stream()
                .map(tweet -> generateResponseWithMedia(tweet))
                .toList();

        return new AllTweetsResponse(tweetResponses);
    }

    public TweetResponse generateResponseWithMedia(Tweet tweet) {
        List<MediaResponse> media = mediaService.getMediaForTweet(tweet);

        return new TweetResponse(
                tweet.getId(),
                tweet.getUser().getUserName(),
                tweet.getContent(),
                tweet.getCreatedAt(),
                media
        );
    }

    @Transactional
    public void deleteTweet(Long tweetId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found."));

        mediaService.deleteTweetMedia(tweet);

        tweetRepository.delete(tweet);
    }
}
