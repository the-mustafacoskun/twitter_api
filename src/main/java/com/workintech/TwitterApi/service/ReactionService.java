package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.ReactionResponse;
import com.workintech.TwitterApi.dto.TweetReactionCountResponse;
import com.workintech.TwitterApi.entity.ReactionType;

import java.util.List;
import java.util.Map;

public interface ReactionService {

    ReactionResponse findByUserIdAndTweetId(Long userId, Long tweetId);
    Map<Long,Integer> getTweetLikeCounts();
    Map<Long,Integer> getTweetDislikeCounts();
    ReactionResponse toggleReaction(Long userId, Long tweetId, ReactionType reactionType);

}
