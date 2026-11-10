package com.workintech.TwitterApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TweetReactionCountResponse {
    private Long tweetId;
    private Long reactionCount;
}