package com.workintech.TwitterApi.dto;

public class CommentCountResponse {
    private Long tweetId;
    private Integer count;
    public CommentCountResponse(Long tweetId, Integer count) {
        this.tweetId = tweetId;
        this.count = count;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public Integer getCount() {
        return count;
    }
}
