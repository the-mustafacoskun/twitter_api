package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.ReactionResponse;
import com.workintech.TwitterApi.dto.TweetReactionCountResponse;
import com.workintech.TwitterApi.entity.Reaction;
import com.workintech.TwitterApi.entity.ReactionType;
import com.workintech.TwitterApi.exception.ApiException;
import com.workintech.TwitterApi.repository.ReactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired


    @Override
    public ReactionResponse findByUserIdAndTweetId(Long userId, Long tweetId) {
        Optional<Reaction> optionalReaction =reactionRepository.findByUserIdAndTweetId(userId,tweetId);
        if(optionalReaction.isPresent()){
            Reaction reaction = optionalReaction.get();
            return new ReactionResponse(reaction.getTweet().getId(),reaction.getReactionType());
        }
        return null;

    }

    @Override
    public Map<Long,Integer> getTweetLikeCounts() {
        List<TweetReactionCountResponse> tweetReactionCountResponseList = reactionRepository.getTweetLikeCounts();
        Map<Long,Integer> tweetLikesCountResponseMap = new HashMap<>();
        for(TweetReactionCountResponse item : tweetReactionCountResponseList) {
            tweetLikesCountResponseMap.put(item.getTweetId(),item.getReactionCount());
        }
        return tweetLikesCountResponseMap;
    }

    @Override
    public Map<Long,Integer> getTweetDislikeCounts() {
        List<TweetReactionCountResponse> tweetReactionCountResponseList = reactionRepository.getTweetDislikeCounts();
        Map<Long,Integer> tweetDisLikesCountResponseMap = new HashMap<>();
        for(TweetReactionCountResponse item : tweetReactionCountResponseList) {
            tweetDisLikesCountResponseMap.put(item.getTweetId(),item.getReactionCount());
        }
        return tweetDisLikesCountResponseMap;
    }


    public ReactionResponse toggleReaction(Long userId, Long tweetId, ReactionType reactionType) {
        Reaction reaction = new Reaction();
       return null;
    }
}
