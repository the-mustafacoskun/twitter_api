package com.workintech.TwitterApi.repository;

import com.workintech.TwitterApi.dto.CommentCountResponse;
import com.workintech.TwitterApi.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.tweet.id = :tweetId ORDER BY c.createdAt DESC")
    List<Comment> findByTweetIdOrderByCreatedAtDesc(Long tweetId);

    void deleteByTweet_Id(Long tweetId);

    @Query("SELECT new com.workintech.TwitterApi.dto.CommentCountResponse(c.tweet.id,COUNT(c)) FROM Comment c GROUP BY c.tweet.id")
    List<CommentCountResponse> getCommentCounts();
}
