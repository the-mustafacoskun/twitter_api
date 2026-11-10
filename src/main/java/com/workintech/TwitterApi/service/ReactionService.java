package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.ReactionResponse;
import com.workintech.TwitterApi.entity.ReactionType;

import java.util.Map;

public interface ReactionService {
    ReactionResponse findByUsernameAndTweetId(String username, Long tweetId);
    Map<Long, Integer> getTweetLikeCounts();
    Map<Long, Integer> getTweetDislikeCounts();
    ReactionResponse toggleReaction(String username, Long tweetId, ReactionType reactionType);
    Long deleteByTweet_Id(Long tweetId);
}