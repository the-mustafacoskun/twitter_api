package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.RetweetCountResponse;
import com.workintech.TwitterApi.dto.RetweetResponse;
import com.workintech.TwitterApi.entity.Retweet;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RetweetService {

    Map<Long,Integer> getRetweetCount();
    Optional<Retweet> findByUser_IdAndTweet_Id(Long userId, Long tweetId);
    Long deleteByTweet_Id(Long tweetId);
    List<RetweetResponse> findByUserId(Long userId);
}
