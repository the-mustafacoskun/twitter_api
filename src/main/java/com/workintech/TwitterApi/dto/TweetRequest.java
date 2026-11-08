package com.workintech.TwitterApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TweetRequest(@NotBlank @Size(max=200) String content) {
}
