package com.workintech.TwitterApi.repository;

import com.workintech.TwitterApi.dto.RetweetCountResponse;
import com.workintech.TwitterApi.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    @Query("SELECT new com.workintech.TwitterApi.dto.RetweetCountResponse(r.tweet.id,COUNT(r)) FROM Retweet r GROUP BY r.tweet.id ")
    List<RetweetCountResponse> getRetweetCount();
    boolean existsByUser_IdAndTweet_Id(Long userId, Long tweetId);
    long countByTweet_Id(Long tweetId);
    void deleteByTweetId(Long tweetId);
}
