package com.workintech.TwitterApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCountResponse {
    private Long tweetId;
    private Long count;
}