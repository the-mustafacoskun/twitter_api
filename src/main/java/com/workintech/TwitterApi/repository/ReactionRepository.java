package com.workintech.TwitterApi.repository;

import com.workintech.TwitterApi.dto.TweetReactionCountResponse;
import com.workintech.TwitterApi.entity.Reaction;
import com.workintech.TwitterApi.entity.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByUser_UsernameAndTweet_Id(String username, Long tweetId);

    @Query("SELECT new com.workintech.TwitterApi.dto.TweetReactionCountResponse(r.tweet.id, COUNT(r)) " +
            "FROM Reaction r WHERE r.reactionType = :type GROUP BY r.tweet.id")
    List<TweetReactionCountResponse> getReactionCounts(@Param("type") ReactionType type);

    Long deleteByTweet_Id(Long tweetId);
}