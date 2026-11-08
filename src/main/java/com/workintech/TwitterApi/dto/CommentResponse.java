package com.workintech.TwitterApi.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long tweetId,
        String username) {
}