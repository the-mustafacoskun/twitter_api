package com.workintech.TwitterApi.dto;

import java.time.LocalDateTime;

public record RetweetResponse(Long id, Long tweetId, String username, LocalDateTime createdAt) {
}