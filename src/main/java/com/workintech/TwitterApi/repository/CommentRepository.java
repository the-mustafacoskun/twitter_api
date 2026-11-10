package com.workintech.TwitterApi.repository;

import com.workintech.TwitterApi.dto.CommentCountResponse;
import com.workintech.TwitterApi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTweet_IdOrderByCreatedAtDesc(Long tweetId);

    Long deleteByTweet_Id(Long tweetId);

    @Query("SELECT new com.workintech.TwitterApi.dto.CommentCountResponse(c.tweet.id, COUNT(c)) " +
            "FROM Comment c GROUP BY c.tweet.id")
    List<CommentCountResponse> getCommentCounts();
}