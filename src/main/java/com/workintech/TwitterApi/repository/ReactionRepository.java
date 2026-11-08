package com.workintech.TwitterApi.repository;

import com.workintech.TwitterApi.dto.TweetReactionCountResponse;
import com.workintech.TwitterApi.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query("SELECT r FROM Reaction r WHERE r.user.id = :userId AND r.tweet.id=:tweetId")
    Optional<Reaction>  findByUserIdAndTweetId(Long userId, Long tweetId);

    @Query("SELECT new com.workintech.TwitterApi.dto.TweetReactionCountResponse(r.tweet.id,COUNT(r)) FROM Reaction r WHERE r.type='LIKE' GROUP BY r.tweet.id ")
    List<TweetReactionCountResponse> getTweetLikeCounts();

    @Query("SELECT new com.workintech.TwitterApi.dto.TweetReactionCountResponse(r.tweet.id,COUNT(r)) FROM Reaction r WHERE r.type='DISLIKE' GROUP BY r.tweet.id ")
    List<TweetReactionCountResponse> getTweetDislikeCounts();
    Long deleteByTweet_Id(Long tweetId);
}
