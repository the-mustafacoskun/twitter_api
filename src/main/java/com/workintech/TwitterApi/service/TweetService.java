package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.TweetResponse;

import java.util.List;

public interface TweetService {
    List<TweetResponse> findByUsername(String username);
    List<TweetResponse> findAllByOrderByCreatedAtDesc();
    List<TweetResponse> findByUserIdOrderByCreatedAtDesc(Long userId);
    TweetResponse findById(Long id);
    TweetResponse create(String content, String username);
    TweetResponse update(Long id, String content, String username);
    void delete(Long id, String username);
}