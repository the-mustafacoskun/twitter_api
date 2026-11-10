package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.CommentRequest;
import com.workintech.TwitterApi.dto.CommentResponse;

import java.util.List;
import java.util.Map;

public interface CommentService {
    List<CommentResponse> findByTweetIdOrderByCreatedAtDesc(Long tweetId);
    Map<Long, Integer> getCommentCountMap();
    CommentResponse findById(Long id);
    CommentResponse save(Long tweetId, CommentRequest request, String username);
    CommentResponse update(Long id, CommentRequest request, String username);
    Long deleteByTweet_Id(Long tweetId);
    Long deleteById(Long id, String username);
}