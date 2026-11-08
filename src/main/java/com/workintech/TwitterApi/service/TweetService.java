package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.TweetRequest;
import com.workintech.TwitterApi.dto.TweetResponse;
import com.workintech.TwitterApi.entity.Tweet;

import java.util.List;

public interface TweetService {
    List<TweetResponse> findByUsername(String username);
    List<TweetResponse> findAllByOrderByCreatedAtDesc();
    List<TweetResponse> findByUserIdOrderByCreatedAtDesc(Long userId);
    TweetResponse  findById(Long id);
    TweetResponse save(TweetRequest tweetRequest);
    TweetResponse update(Long Id,TweetRequest tweetRequest);
    void deleteById(Long id);

}
