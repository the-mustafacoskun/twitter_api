package com.workintech.TwitterApi.dto;

public class RetweetCountResponse {
    private Long tweetId;
    private Integer count;
    public RetweetCountResponse(Long tweetId, Integer count) {
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
