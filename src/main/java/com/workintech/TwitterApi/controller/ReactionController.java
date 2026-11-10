package com.workintech.TwitterApi.controller;

import com.workintech.TwitterApi.dto.ReactionResponse;
import com.workintech.TwitterApi.entity.ReactionType;
import com.workintech.TwitterApi.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/like")
    public ReactionResponse like(@RequestParam Long tweetId, Authentication authentication) {
        return reactionService.toggleReaction(authentication.getName(), tweetId, ReactionType.LIKE);
    }

    @PostMapping("/dislike")
    public ReactionResponse dislike(@RequestParam Long tweetId, Authentication authentication) {
        return reactionService.toggleReaction(authentication.getName(), tweetId, ReactionType.DISLIKE);
    }

    @GetMapping("/reaction")
    public ReactionResponse myReaction(@RequestParam Long tweetId, Authentication authentication) {
        return reactionService.findByUsernameAndTweetId(authentication.getName(), tweetId);
    }
}