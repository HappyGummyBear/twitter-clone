package com.business.twitter.repository;

import com.business.twitter.entity.Media;
import com.business.twitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByTweet(Tweet tweet);
}
