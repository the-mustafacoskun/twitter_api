package com.workintech.TwitterApi.dto;

public class TweetReactionCountResponse {
    private Long TweetId;
    private Integer ReactionCount;
    public TweetReactionCountResponse(Long TweetId, Integer reactionCount) {
        this.TweetId = TweetId;
        this.ReactionCount = reactionCount;
    }

    public Long getTweetId() {
        return TweetId;
    }

    public Integer getReactionCount() {
        return ReactionCount;
    }
}
