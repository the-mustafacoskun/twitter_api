package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.CommentCountResponse;
import com.workintech.TwitterApi.dto.CommentRequest;
import com.workintech.TwitterApi.dto.CommentResponse;
import com.workintech.TwitterApi.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {
    List<CommentResponse> findByTweetIdOrderByCreatedAtDesc(Long tweetId);

    Map<Long,Integer> getCommentCountMap();
    CommentResponse findById(Long id);
    CommentResponse save(Long tweetId,CommentRequest commentRequest);
    CommentResponse update(Long id,CommentRequest commentRequest);
    Long deleteByTweet_Id(Long id);
    Long deleteById(Long id);

}
