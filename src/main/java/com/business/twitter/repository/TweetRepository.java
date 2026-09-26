package com.business.twitter.repository;

import com.business.twitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet,Long> {
    List<Tweet> findByUserIdOrderByCreatedAtDesc(Long userId);
}
