package com.workintech.TwitterApi.repository;

import com.workintech.TwitterApi.dto.RetweetCountResponse;
import com.workintech.TwitterApi.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    @Query("SELECT new com.workintech.TwitterApi.dto.RetweetCountResponse(r.tweet.id,COUNT(r)) FROM Retweet r GROUP BY r.tweet.id ")
    List<RetweetCountResponse> getRetweetCount();
    Optional<Retweet> findByUser_IdAndTweet_Id(Long userId, Long tweetId);
    List<Retweet> findByUser_Id(Long userId);
    Long deleteByTweet_Id(Long tweetId);
    void deleteByUser_IdAndTweet_Id(Long userId, Long tweetId);
}
