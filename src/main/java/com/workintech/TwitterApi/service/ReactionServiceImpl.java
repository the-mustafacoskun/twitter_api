package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.ReactionResponse;
import com.workintech.TwitterApi.dto.TweetReactionCountResponse;
import com.workintech.TwitterApi.entity.Reaction;
import com.workintech.TwitterApi.entity.ReactionType;
import com.workintech.TwitterApi.entity.Tweet;
import com.workintech.TwitterApi.exception.ApiException;
import com.workintech.TwitterApi.repository.ReactionRepository;
import com.workintech.TwitterApi.repository.TweetRepository;
import com.workintech.TwitterApi.repository.UserRepository;
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
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

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
        Optional<Reaction> optionalReaction = reactionRepository.findByUserIdAndTweetId(userId,tweetId);
        if(optionalReaction.isPresent()){
            Reaction reaction = optionalReaction.get();
            if(reaction.getReactionType().equals(reactionType)){
                reactionRepository.deleteById(reaction.getId());
                return null;
            }else{
                reaction.setReactionType(reactionType);
                reactionRepository.save(reaction);
                return new ReactionResponse(reaction.getTweet().getId(),reaction.getReactionType());
            }

        }else{
            Reaction reaction = new Reaction();
            reaction.setReactionType(reactionType);
            reaction.setTweet(tweetRepository.findById(tweetId).orElseThrow(()->new ApiException("Tweet bulunamadı",HttpStatus.NOT_FOUND)));
            reaction.setUser(userRepository.findById(userId).orElseThrow(()->new ApiException("User Bulunamadı",HttpStatus.NOT_FOUND)));
            reactionRepository.save(reaction);
            return new ReactionResponse(reaction.getTweet().getId(),reaction.getReactionType());
        }
    }

    @Override
    public Long deleteByTweet_Id(Long tweetId) {
        return reactionRepository.deleteByTweet_Id(tweetId);

    }
}
