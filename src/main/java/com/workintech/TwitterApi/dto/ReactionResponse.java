package com.workintech.TwitterApi.dto;

import com.workintech.TwitterApi.entity.ReactionType;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

// Like, dislike  isteklerinde
public record ReactionResponse(@NotNull Long tweetId,@NotNull ReactionType reactionType) {
}