package com.workintech.TwitterApi.dto;

import java.time.LocalDateTime;

public record TweetResponse(Long id, String content, LocalDateTime createdAt,Long userId,String userName,long likeCount,long dislikeCount,long retweetCount,long commentCount) {
}
