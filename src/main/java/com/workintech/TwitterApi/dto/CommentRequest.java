package com.workintech.TwitterApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotNull Long tweetId,
        @NotBlank @Size(max = 280) String content) {
}