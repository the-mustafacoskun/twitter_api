package com.workintech.TwitterApi.repository;

import com.workintech.TwitterApi.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByUsername(String username);
    List<Tweet> findAllByOrderByCreatedAtDesc();
    List<Tweet> findByUser_IdOrderByCreatedAtDesc(Long userId);

}
