package com.workintech.TwitterApi.dto;

public class RetweetCountResponse {

    private Long tweetId;
    private int count;

    // Hibernate COUNT(...) için Long gönderir, burada int'e çeviriyoruz
    public RetweetCountResponse(Long tweetId, Long count) {
        this.tweetId = tweetId;
        this.count = count.intValue();
    }

    public Long getTweetId() {
        return tweetId;
    }

    public int getCount() {
        return count;
    }
}