package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.*;
import com.workintech.TwitterApi.entity.Tweet;
import com.workintech.TwitterApi.entity.User;
import com.workintech.TwitterApi.exception.ApiException;
import com.workintech.TwitterApi.repository.*;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class TweetServiceImpl implements TweetService {
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private RetweetService retweetService;

    @Autowired
    private CommentService commentService;


    //Helper method for Counting (To avoid to repeat)

    private TweetResponse mapToTweetResponse(Tweet tweet) {
        Map<Long,Integer> likeCountMap = reactionService.getTweetLikeCounts();
        Map<Long,Integer> disLikeCountMap = reactionService.getTweetDislikeCounts();
        Map<Long,Integer> retweetCountMap = retweetService.getRetweetCount();
        Map<Long,Integer> commentCountMap =  commentService.getCommentCountMap();

        return new  TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getCreatedAt(),
                tweet.getUser().getId(),
                tweet.getUser().getUserName(),
                likeCountMap.getOrDefault(tweet.getId(),0),
                disLikeCountMap.getOrDefault(tweet.getId(),0),
                retweetCountMap.getOrDefault(tweet.getId(),0),
                commentCountMap.getOrDefault(tweet.getId(),0)
        );
    }

    private List<TweetResponse> getTweetResponseList(List<Tweet> tweets) {
        List<TweetResponse> tweetResponseList = new ArrayList<>();
        for (Tweet tweet : tweets) {
            tweetResponseList.add(mapToTweetResponse(tweet));
        }
        return tweetResponseList;
    }



    @Override
    public List<TweetResponse> findByUsername(String username) {
        List<Tweet> tweets = tweetRepository.findByUsername(username);
        return getTweetResponseList(tweets);


    }

    @Override
    public List<TweetResponse> findAllByOrderByCreatedAtDesc() {
        List<Tweet> tweets = tweetRepository.findAllByOrderByCreatedAtDesc();
        return getTweetResponseList(tweets);

    }

    @Override
    public List<TweetResponse> findByUserIdOrderByCreatedAtDesc(Long userId) {
        List<Tweet> tweets = tweetRepository.findByUser_IdOrderByCreatedAtDesc(userId);
        return getTweetResponseList(tweets);
    }



    @Override
    public TweetResponse findById(Long id) {
        Tweet tweet = tweetRepository.findById(id).orElseThrow(()->new ApiException("Tweet Bulunamadı", HttpStatus.NOT_FOUND));
        return mapToTweetResponse(tweet);
    }

    @Override
    public TweetResponse save(TweetRequest tweetRequest) {
        Tweet tweet = new Tweet();
        tweet.setContent(tweetRequest.content());
        //TODO securityi kurduktan sonra user bilgileri çek
        tweet.setUser(new User());

        tweetRepository.save(tweet);
        return mapToTweetResponse(tweet);
    }

    @Override
    public TweetResponse update(Long id, TweetRequest tweetRequest) {
        Tweet tweet =tweetRepository.findById(id).orElseThrow(()->new ApiException("Tweet Bulunamadı", HttpStatus.NOT_FOUND));
        tweet.setContent(tweetRequest.content());
        return mapToTweetResponse(tweetRepository.save(tweet));
    }

    @Override
    public Long deleteById(Long id) {
        retweetService.deleteByTweet_Id(id);
        reactionService.deleteByTweet_Id(id);
        commentService.deleteByTweet_Id(id);
        tweetRepository.deleteById(id);

        return id;
    }
}
