package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.ReactionResponse;
import com.workintech.TwitterApi.dto.TweetReactionCountResponse;
import com.workintech.TwitterApi.entity.Reaction;
import com.workintech.TwitterApi.entity.ReactionType;
import com.workintech.TwitterApi.entity.Tweet;
import com.workintech.TwitterApi.entity.User;
import com.workintech.TwitterApi.exception.ApiException;
import com.workintech.TwitterApi.repository.ReactionRepository;
import com.workintech.TwitterApi.repository.TweetRepository;
import com.workintech.TwitterApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ReactionResponse findByUsernameAndTweetId(String username, Long tweetId) {
        ReactionType type = reactionRepository.findByUser_UsernameAndTweet_Id(username, tweetId)
                .map(Reaction::getReactionType)
                .orElse(null);
        return new ReactionResponse(tweetId, type);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Integer> getTweetLikeCounts() {
        return toCountMap(reactionRepository.getReactionCounts(ReactionType.LIKE));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Integer> getTweetDislikeCounts() {
        return toCountMap(reactionRepository.getReactionCounts(ReactionType.DISLIKE));
    }

    @Override
    public ReactionResponse toggleReaction(String username, Long tweetId, ReactionType reactionType) {
        Optional<Reaction> existing = reactionRepository.findByUser_UsernameAndTweet_Id(username, tweetId);

        if (existing.isPresent()) {
            Reaction reaction = existing.get();
            if (reaction.getReactionType() == reactionType) {
                // Aynı reaksiyon tekrar gönderildi: geri al
                reactionRepository.delete(reaction);
                return new ReactionResponse(tweetId, null);
            }
            // Farklı reaksiyon: like <-> dislike değişimi
            reaction.setReactionType(reactionType);
            reactionRepository.save(reaction);
            return new ReactionResponse(tweetId, reactionType);
        }

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ApiException("Tweet bulunamadı", HttpStatus.NOT_FOUND));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND));

        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setTweet(tweet);
        reaction.setReactionType(reactionType);
        reactionRepository.save(reaction);
        return new ReactionResponse(tweetId, reactionType);
    }

    @Override
    public Long deleteByTweet_Id(Long tweetId) {
        return reactionRepository.deleteByTweet_Id(tweetId);
    }

    private Map<Long, Integer> toCountMap(List<TweetReactionCountResponse> items) {
        Map<Long, Integer> map = new HashMap<>();
        for (TweetReactionCountResponse item : items) {
            map.put(item.getTweetId(), item.getReactionCount().intValue());
        }
        return map;
    }
}